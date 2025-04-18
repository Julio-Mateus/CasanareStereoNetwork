package com.jcmateus.casanarestereo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.google.firebase.appcheck.BuildConfig
import com.google.firebase.appcheck.FirebaseAppCheck
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.DataStoreManager
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
import com.google.firebase.storage.FirebaseStorage

// Define the DataStore as a top-level extension function
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreManager.DATASTORE_NAME)
class HomeApplication : Application() {
    val navController: NavHostController by lazy {
        NavHostController(this).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
            setViewModelStore(ViewModelStore())
        }
    }

    val firebaseAuth: FirebaseAuth by lazy {
        Log.d("HomeApplication", "firebaseAuth: Creando instancia de FirebaseAuth")
        FirebaseAuth.getInstance()
    }
    var showScaffold by mutableStateOf(false)

    val dataStoreManager: DataStoreManager by lazy {
        Log.d("HomeApplication", "dataStoreManager: Creando instancia de DataStoreManager")
        DataStoreManager(this.dataStore) // Usar la extensión dataStore
    } // Instancia única de DataStoreManager
    val db: FirebaseFirestore by lazy {
        Log.d("HomeApplication", "db: Creando instancia de FirebaseFirestore")
        FirebaseFirestore.getInstance()
    }
    val storage: FirebaseStorage by lazy {
        Log.d("HomeApplication", "storage: Creando instancia de FirebaseStorage")
        FirebaseStorage.getInstance()
    }
    val authService: AuthService by lazy {
        Log.d("HomeApplication", "authService: Creando instancia de AuthService")
        AuthService(firebaseAuth, db, dataStoreManager)
    }
    val emisoraRepository: EmisoraRepository by lazy {
        Log.d("HomeApplication", "emisoraRepository: Creando instancia de EmisoraRepository")
        EmisoraRepository(db, storage) // Corregido: solo necesita db y storage
    }
    val emisoraViewModelFactory: EmisoraViewModelFactory by lazy {
        Log.d("HomeApplication", "emisoraViewModelFactory: Creando instancia de EmisoraViewModelFactory")
        EmisoraViewModelFactory(emisoraRepository, firebaseAuth, db)
    }
    // Add this line to create the factory
    val programaRepository: ProgramaRepository by lazy {
        Log.d("HomeApplication", "programaRepository: Creando instancia de ProgramaRepository")
        ProgramaRepository(db)
    }
    val programaViewModelFactory: ProgramaViewModelFactory by lazy {
        Log.d("HomeApplication", "programaViewModelFactory: Creando instancia de ProgramaViewModelFactory")
        ProgramaViewModelFactory(programaRepository, firebaseAuth, db)
    }

    // Add this line to create the factory
    val noticiaRepository: NoticiaRepository by lazy {
        Log.d("HomeApplication", "noticiaRepository: Creando instancia de NoticiaRepository")
        NoticiaRepository(db)
    }
    val noticiaViewModelFactory: NoticiaViewModelFactory by lazy {
        Log.d("HomeApplication", "noticiaViewModelFactory: Creando instancia de NoticiaViewModelFactory")
        NoticiaViewModelFactory(noticiaRepository, firebaseAuth, db, storage, authService)
    }

    val emisoraViewModel: EmisoraViewModel by lazy {
        Log.d("HomeApplication", "emisoraViewModel: Creando instancia de EmisoraViewModel")
        emisoraViewModelFactory.create(EmisoraViewModel::class.java)
    }
    val podcastViewModel: PodcastViewModel by lazy {
        Log.d("HomeApplication", "podcastViewModel: Creando instancia de PodcastViewModel")
        podcastViewModelFactory.create(PodcastViewModel::class.java)
    }

    val podcastRepository: PodcastRepository by lazy {
        Log.d("HomeApplication", "podcastRepository: Creando instancia de PodcastRepository")
        PodcastRepository(db)
    }
    val podcastViewModelFactory by lazy {
        Log.d("HomeApplication", "podcastViewModelFactory: Creando instancia de PodcastViewModelFactory")
        PodcastViewModelFactory(
            podcastRepository, // Use the instance created above
            firebaseAuth,
            db
        )
    }

    // Añadir UsuarioRepository y UsuarioViewModel
    val usuarioRepository: UsuarioRepository by lazy {
        Log.d("HomeApplication", "usuarioRepository: Creando instancia de UsuarioRepository")
        UsuarioRepository(db)
    }
    val usuarioViewModel: UsuarioViewModel by lazy {
        Log.d("HomeApplication", "usuarioViewModel: Creando instancia de UsuarioViewModel")
        UsuarioViewModel(usuarioRepository)
    }
    val usuarioPerfilViewModel: UsuarioPerfilViewModel by lazy {
        Log.d("HomeApplication", "usuarioPerfilViewModel: Creando instancia de UsuarioPerfilViewModel")
        UsuarioPerfilViewModel(usuarioRepository, authService, db, storage)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("HomeApplication", "onCreate: HomeApplication creado")
    }
}