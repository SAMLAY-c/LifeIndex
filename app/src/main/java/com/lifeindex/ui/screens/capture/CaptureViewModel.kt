package com.lifeindex.ui.screens.capture

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lifeindex.data.entity.ItemEntity
import com.lifeindex.data.repository.ItemRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

sealed interface CaptureState {
    data object Idle : CaptureState
    data object Analyzing : CaptureState
    data class Confirming(
        val imagePath: String,
        val title: String,
        val category: String,
        val tags: List<String>,
        val confidence: Float,
        val analysis: String
    ) : CaptureState
    data object Saved : CaptureState
}

class CaptureViewModel(
    application: Application,
    private val repository: ItemRepository
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val state: StateFlow<CaptureState> = _state.asStateFlow()

    private var currentPhotoPath: String? = null
    private var currentPhotoUri: Uri? = null

    fun createImageUri(): Pair<Uri, String> {
        val storageDir = File(getApplication<Application>().getExternalFilesDir(null), "images")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val photoFile = File(storageDir, "temp_capture_${System.currentTimeMillis()}.jpg")
        currentPhotoPath = photoFile.absolutePath

        val authority = "${getApplication<Application>().packageName}.fileprovider"
        currentPhotoUri = androidx.core.content.FileProvider.getUriForFile(
            getApplication(),
            authority,
            photoFile
        )

        return Pair(currentPhotoUri!!, currentPhotoPath!!)
    }

    fun analyzeImage(imagePath: String) {
        viewModelScope.launch {
            _state.value = CaptureState.Analyzing
            currentPhotoPath = imagePath

            // Simulate AI analysis
            delay(2000)

            // Mock AI results - replace with actual AI integration
            val mockResult = CaptureState.Confirming(
                imagePath = imagePath,
                title = "检测到的物品",
                category = "未分类",
                tags = listOf("待编辑", "新物品"),
                confidence = 0.85f,
                analysis = "这是一件待识别的物品，请编辑相关信息。"
            )

            _state.value = mockResult
        }
    }

    fun updateTitle(title: String) {
        val currentState = _state.value
        if (currentState is CaptureState.Confirming) {
            _state.value = currentState.copy(title = title)
        }
    }

    fun updateCategory(category: String) {
        val currentState = _state.value
        if (currentState is CaptureState.Confirming) {
            _state.value = currentState.copy(category = category)
        }
    }

    fun addTag(tag: String) {
        val currentState = _state.value
        if (currentState is CaptureState.Confirming) {
            _state.value = currentState.copy(tags = currentState.tags + tag)
        }
    }

    fun removeTag(tag: String) {
        val currentState = _state.value
        if (currentState is CaptureState.Confirming) {
            _state.value = currentState.copy(tags = currentState.tags - tag)
        }
    }

    fun saveItem() {
        val currentState = _state.value
        if (currentState is CaptureState.Confirming) {
            viewModelScope.launch {
                try {
                    // Move temp file to permanent location
                    val storageDir = File(getApplication<Application>().getExternalFilesDir(null), "images")
                    if (!storageDir.exists()) {
                        storageDir.mkdirs()
                    }

                    val tempFile = File(currentState.imagePath)
                    val permanentFile = File(storageDir, "item_${System.currentTimeMillis()}.jpg")

                    tempFile.copyTo(permanentFile, overwrite = true)
                    tempFile.delete()

                    // Create item entity
                    val item = ItemEntity(
                        imagePath = permanentFile.absolutePath,
                        title = currentState.title,
                        category = currentState.category,
                        tagsJson = currentState.tags.joinToString(","),
                        analysis = currentState.analysis
                    )

                    repository.insertItem(item)
                    _state.value = CaptureState.Saved
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun reset() {
        _state.value = CaptureState.Idle
        currentPhotoPath = null
        currentPhotoUri = null
    }
}
