package com.lifeindex.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lifeindex.data.entity.ItemEntity
import com.lifeindex.data.repository.ItemRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val items: List<ItemEntity> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "全部"
)

class HomeViewModel(
    application: Application,
    private val repository: ItemRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getAllItems().collect { items ->
                _uiState.update { currentState ->
                    currentState.copy(
                        items = filterItems(items, currentState.searchQuery, currentState.selectedCategory),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilters()
    }

    private fun applyFilters() {
        viewModelScope.launch {
            repository.getAllItems().collect { items ->
                val filtered = filterItems(items, _uiState.value.searchQuery, _uiState.value.selectedCategory)
                _uiState.update { it.copy(items = filtered) }
            }
        }
    }

    private fun filterItems(
        items: List<ItemEntity>,
        query: String,
        category: String
    ): List<ItemEntity> {
        var result = items

        // Filter by category
        if (category != "全部") {
            result = result.filter { it.category.contains(category, ignoreCase = true) }
        }

        // Filter by search query
        if (query.isNotBlank()) {
            result = result.filter { item ->
                item.title.contains(query, ignoreCase = true) ||
                        item.tagsJson.contains(query, ignoreCase = true)
            }
        }

        return result
    }
}
