package com.lifeindex.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lifeindex.data.database.AppDatabase
import com.lifeindex.data.repository.ItemRepository
import com.lifeindex.ui.screens.detail.DetailViewModel
import com.lifeindex.ui.screens.home.HomeViewModel
import com.lifeindex.ui.screens.capture.CaptureViewModel

class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    private val database by lazy { AppDatabase.getDatabase(application) }
    private val _repository by lazy { ItemRepository(database.itemDao()) }

    val repository: ItemRepository
        get() = _repository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(application, repository) as T
            CaptureViewModel::class.java -> CaptureViewModel(application, repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    fun createDetailViewModel(itemId: Long): DetailViewModel {
        return DetailViewModel(application, repository, itemId)
    }
}

// Extension functions for ViewModels to use with factory
fun HomeViewModel.provideFactory(factory: ViewModelFactory): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(factory.application, factory.repository) as T
        }
    }

fun CaptureViewModel.provideFactory(factory: ViewModelFactory): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CaptureViewModel(factory.application, factory.repository) as T
        }
    }

fun DetailViewModel.provideFactory(factory: ViewModelFactory, itemId: Long): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.createDetailViewModel(itemId) as T
        }
    }

// Access to application property
val ViewModelFactory.application: Application
    get() = application
