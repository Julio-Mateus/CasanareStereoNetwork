package com.jcmateus.casanarestereo

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.jcmateus.casanarestereo.screens.login.DataStoreManager


class HomeApplication : Application() {
    val navController: NavHostController by lazy {
        NavHostController(this).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
            setViewModelStore(ViewModelStore())
        }
    }
    var showScaffold by mutableStateOf(false)

    val dataStoreManager: DataStoreManager by lazy { DataStoreManager(this) } // Instancia única de DataStoreManager

    override fun onCreate() {
        super.onCreate()
    }
}
