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
import com.google.firebase.appcheck.BuildConfig
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.DataStoreManager
import com.jcmateus.casanarestereo.screens.login.dataStore
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraRepository
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModelFactory
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias.NoticiaRepository
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias.NoticiaViewModelFactory
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.PodcastRepository
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.PodcastViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.PodcastViewModelFactory
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion.ProgramaRepository
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion.ProgramaViewModelFactory
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioPerfilViewModel
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioRepository
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


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

    val dataStoreManager: DataStoreManager by lazy { DataStoreManager(this.dataStore) } // Instancia única de DataStoreManager
    val authService: AuthService by lazy { AuthService(firebaseAuth, dataStoreManager) }
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val emisoraRepository: EmisoraRepository by lazy { EmisoraRepository(firebaseAuth, db) }
    val emisoraViewModelFactory: EmisoraViewModelFactory by lazy {
        EmisoraViewModelFactory(emisoraRepository, firebaseAuth)
    }
    // Add this line to create the factory
    val programaRepository: ProgramaRepository by lazy { ProgramaRepository(db) }
    val programaViewModelFactory: ProgramaViewModelFactory by lazy {
        ProgramaViewModelFactory(programaRepository, firebaseAuth, db)
    }

    // Add this line to create the factory
    val noticiaRepository: NoticiaRepository by lazy { NoticiaRepository(db) }
    val noticiaViewModelFactory: NoticiaViewModelFactory by lazy {
        NoticiaViewModelFactory(noticiaRepository, firebaseAuth, db)
    }

    val emisoraViewModel: EmisoraViewModel by lazy {
        emisoraViewModelFactory.create(EmisoraViewModel::class.java)
    }
    val podcastViewModel: PodcastViewModel by lazy {
        podcastViewModelFactory.create(PodcastViewModel::class.java)
    }

    val podcastRepository: PodcastRepository by lazy { PodcastRepository(db) }
    val podcastViewModelFactory by lazy {
        PodcastViewModelFactory(
            PodcastRepository(FirebaseFirestore.getInstance()), // Pasa la instancia de FirebaseFirestore aquí
            FirebaseAuth.getInstance(),
            FirebaseFirestore.getInstance()
        )
    }

    // Añadir UsuarioRepository y UsuarioViewModel
    val usuarioRepository: UsuarioRepository by lazy { UsuarioRepository() }
    val usuarioViewModel: UsuarioViewModel by lazy { UsuarioViewModel(usuarioRepository) }
    val usuarioPerfilViewModel: UsuarioPerfilViewModel by lazy { UsuarioPerfilViewModel(usuarioRepository, authService) }

    override fun onCreate() {
        super.onCreate()
    }
}