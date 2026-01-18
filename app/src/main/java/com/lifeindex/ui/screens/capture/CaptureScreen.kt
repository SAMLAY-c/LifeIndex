package com.lifeindex.ui.screens.capture

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lifeindex.ui.screens.capture.CaptureState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureAndConfirmScreen(
    state: CaptureState,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit
) {
    var showCategoryDialog by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            if (state is CaptureState.Confirming) {
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("保存并归档")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (state is CaptureState.Confirming) {
                // Image display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    AsyncImage(
                        model = state.imagePath,
                        contentDescription = "Captured Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                when (state) {
                    is CaptureState.Analyzing -> {
                        // Analyzing state
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "AI 正在识别物品特征...",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "正在分析形状、颜色和材质",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    is CaptureState.Confirming -> {
                        // Form state
                        Column(modifier = Modifier.padding(16.dp)) {
                            // AI confidence
                            AssistChip(
                                onClick = {},
                                label = { Text("AI 置信度 ${(state.confidence * 100).toInt()}%") },
                                leadingIcon = { Icon(Icons.Default.Done, null) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Title Input
                            OutlinedTextField(
                                value = state.title,
                                onValueChange = onTitleChange,
                                label = { Text("名称") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Category Selection
                            OutlinedCard(
                                onClick = { showCategoryDialog = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("分类", style = MaterialTheme.typography.labelMedium)
                                        Text(state.category, style = MaterialTheme.typography.bodyLarge)
                                    }
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Tags
                            Text("标签", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                state.tags.forEach { tag ->
                                    InputChip(
                                        selected = true,
                                        onClick = { onRemoveTag(tag) },
                                        label = { Text(tag) },
                                        trailingIcon = {
                                            Icon(Icons.Default.Close, null)
                                        }
                                    )
                                }

                                // Add tag chip
                                if (newTagText.isBlank()) {
                                    SuggestionChip(
                                        onClick = { newTagText = "+" },
                                        label = { Text("+ 添加标签") }
                                    )
                                } else {
                                    OutlinedTextField(
                                        value = newTagText,
                                        onValueChange = { newTagText = it },
                                        label = { Text("新标签") },
                                        modifier = Modifier.width(120.dp),
                                        singleLine = true
                                    )
                                    if (newTagText.isNotBlank() && newTagText != "+") {
                                        Button(
                                            onClick = {
                                                onAddTag(newTagText)
                                                newTagText = ""
                                            },
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Text("添加")
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // AI Analysis
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "✨ AI 分析",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = state.analysis,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    // Category selection dialog
    if (showCategoryDialog && state is CaptureState.Confirming) {
        CategoryDialog(
            currentCategory = state.category,
                            onDismiss = { showCategoryDialog = false },
            onCategorySelected = { category ->
                onCategoryChange(category)
                showCategoryDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    currentCategory: String,
    onDismiss: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("衣服", "工具", "数码", "书籍", "其他", "未分类")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择分类") },
        text = {
            Column {
                categories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(category)
                        RadioButton(
                            selected = category == currentCategory,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                    HorizontalDivider()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
