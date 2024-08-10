package com.jcmateus.casanarestereo

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator


class HomeApplication : Application() {
    lateinit var navController: NavHostController

    override fun onCreate() {
        super.onCreate()
        navController = NavHostController(this) // Inicializa navController aquí
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        navController.navigatorProvider.addNavigator(DialogNavigator())
        val viewModelStore = ViewModelStore()
        navController.setViewModelStore(viewModelStore)
    }
}
