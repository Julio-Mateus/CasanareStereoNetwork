package com.jcmateus.casanarestereo.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginScreenViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {


    override fun <T :ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginScreenViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}