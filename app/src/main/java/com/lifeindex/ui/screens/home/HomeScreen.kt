package com.lifeindex.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lifeindex.ui.components.ItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNavigateToCapture: () -> Unit,
    onItemClick: (Long) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit
) {
    Scaffold(
        topBar = { HomeTopBar(uiState, onSearchQueryChange, onCategoryChange) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCapture,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Capture Item")
            }
        },
        bottomBar = { HomeBottomBar() }
    ) { paddingValues ->
        ItemGridContent(
            uiState = uiState,
            modifier = Modifier.padding(paddingValues),
            onItemClick = onItemClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    uiState: HomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search Bar
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = {},
            active = false,
            onActiveChange = {},
            placeholder = { Text("搜索物品 (如: 蓝色衬衫)") },
            modifier = Modifier.fillMaxWidth()
        ) { }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        val filters = listOf("全部", "衣服", "工具", "数码", "书籍", "其他")
        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters.size) { index ->
                val filterName = filters[index]
                FilterChip(
                    selected = filterName == uiState.selectedCategory,
                    onClick = { onCategoryChange(filterName) },
                    label = { Text(filterName) }
                )
            }
        }
    }
}

@Composable
fun ItemGridContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit
) {
    if (uiState.isLoading && uiState.items.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.items.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = "还没有物品\n点击 + 添加第一个物品",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {
            items(uiState.items.size) { index ->
                ItemCard(
                    item = uiState.items[index],
                    onClick = { onItemClick(uiState.items[index].id) }
                )
            }
        }
    }
}

@Composable
fun HomeBottomBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { /* Navigate */ },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("My Items") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Navigate */ },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}
