package com.eibrahim67.gympro.addData.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository

class AddDataViewModelFactory(private val remoteRepository: RemoteRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddDataViewModel::class.java))
            return AddDataViewModel(remoteRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }

}