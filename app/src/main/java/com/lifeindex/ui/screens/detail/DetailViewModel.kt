package com.lifeindex.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lifeindex.data.entity.ItemEntity
import com.lifeindex.data.repository.ItemRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DetailUiState(
    val item: ItemEntity? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class DetailViewModel(
    application: Application,
    private val repository: ItemRepository,
    private val itemId: Long
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadItem()
    }

    private fun loadItem() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val item = repository.getItemById(itemId)
                _uiState.update { it.copy(item = item, isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            _uiState.value.item?.let { repository.deleteItem(it) }
        }
    }
}
