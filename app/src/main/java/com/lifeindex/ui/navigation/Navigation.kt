package com.lifeindex.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lifeindex.di.ViewModelFactory
import com.lifeindex.ui.screens.capture.CaptureAndConfirmScreen
import com.lifeindex.ui.screens.capture.CaptureScreen
import com.lifeindex.ui.screens.capture.CaptureState
import com.lifeindex.ui.screens.capture.CaptureViewModel
import com.lifeindex.ui.screens.detail.DetailScreen
import com.lifeindex.ui.screens.detail.DetailViewModel
import com.lifeindex.ui.screens.home.HomeScreen
import com.lifeindex.ui.screens.home.HomeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Capture : Screen("capture")
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Long) = "detail/$itemId"
    }
}

@Composable
fun LifeIndexNavigation(
    viewModelFactory: ViewModelFactory
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(viewModelFactory)
            )
            val uiState by viewModel.uiState.collectAsState()

            HomeScreen(
                uiState = uiState,
                onNavigateToCapture = {
                    navController.navigate(Screen.Capture.route)
                },
                onItemClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                },
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onCategoryChange = viewModel::onCategoryChange
            )
        }

        // Capture Screen
        composable(Screen.Capture.route) {
            val viewModel: CaptureViewModel = viewModel(
                factory = CaptureViewModel.provideFactory(viewModelFactory)
            )
            val state by viewModel.state.collectAsState()
            var tempImagePath by remember { mutableStateOf<String?>(null) }

            // Handle camera result
            LaunchedEffect(Unit) {
                tempImagePath?.let { path ->
                    viewModel.analyzeImage(path)
                }
            }

            // Handle back navigation when saved
            if (state is CaptureState.Saved) {
                LaunchedEffect(Unit) {
                    viewModel.reset()
                    navController.popBackStack()
                }
            }

            BackHandler {
                viewModel.reset()
                navController.popBackStack()
            }

            if (state is CaptureState.Confirming || state is CaptureState.Analyzing) {
                CaptureAndConfirmScreen(
                    state = state,
                    onSave = viewModel::saveItem,
                    onNavigateBack = {
                        viewModel.reset()
                        navController.popBackStack()
                    },
                    onTitleChange = viewModel::updateTitle,
                    onCategoryChange = viewModel::updateCategory,
                    onAddTag = viewModel::addTag,
                    onRemoveTag = viewModel::removeTag
                )
            }
        }

        // Detail Screen
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: return@composable
            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.provideFactory(viewModelFactory, itemId)
            )
            val uiState by viewModel.uiState.collectAsState()

            BackHandler {
                navController.popBackStack()
            }

            uiState.item?.let { item ->
                DetailScreen(
                    item = item,
                    onBack = { navController.popBackStack() },
                    onEdit = { /* TODO: Navigate to edit screen */ }
                )
            } ?: run {
                // Loading or error state
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text("Error: ${uiState.error}")
                    }
                }
            }
        }
    }
}
