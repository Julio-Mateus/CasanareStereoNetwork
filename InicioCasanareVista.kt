package com.jcmateus.casanarestereo

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.google.firebase.auth.FirebaseAuth
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.DataStoreManager
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModel


class HomeApplication : Application() {
    val navController: NavHostController by lazy {
        NavHostController(this).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
            setViewModelStore(ViewModelStore())
        }
    }

    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var showScaffold by mutableStateOf(false)

    val dataStoreManager: DataStoreManager by lazy { DataStoreManager(this) } // Instancia única de DataStoreManager
    val authService: AuthService by lazy { AuthService(firebaseAuth) }
    val emisoraViewModelFactory: EmisoraViewModelFactory by lazy {
        EmisoraViewModelFactory(firebaseAuth)
    }

    override fun onCreate() {
        super.onCreate()
    }
}

class EmisoraViewModelFactory(private val firebaseAuth: FirebaseAuth) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmisoraViewModel::class.java)) {
            return EmisoraViewModel(firebaseAuth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
