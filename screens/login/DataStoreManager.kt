package com.jcmateus.casanarestereo.screens.login


import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.text.get
import kotlin.text.set
import kotlin.toString

class DataStoreManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val LOCATION_PERMISSION_GRANTED = booleanPreferencesKey("location_permission_granted")
        val SHOW_DIALOG = booleanPreferencesKey("show_dialog")
        val ROL_USUARIO = stringPreferencesKey("rol_usuario")
        val TERMS_ACCEPTED = booleanPreferencesKey("terms_accepted") // Nueva key
        @SuppressLint("StaticFieldLeak")
        private var instance: DataStoreManager? = null
        fun getInstance(context: Context): DataStoreManager {
            if (instance == null) {
                instance = DataStoreManager(context)
            }
            return instance!!
        }
    }

    fun getTermsAccepted(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[TERMS_ACCEPTED] ?: false
    }

    suspend fun saveTermsAccepted(accepted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TERMS_ACCEPTED] = accepted
        }
    }

    suspend fun guardarRolUsuario(rol: Rol) {
        context.dataStore.edit { preferences ->
            preferences[ROL_USUARIO] = rol.toString()
        }
    }

    fun obtenerRolUsuario(): Flow<Rol> = context.dataStore.data.map { preferences ->
        try {
            Rol.valueOf(preferences[ROL_USUARIO] ?: Rol.USUARIO.toString())
        } catch (e: IllegalArgumentException) {
            Rol.USUARIO // Valor por defecto si el rol no es válido
        }
    }

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    fun getIsLoggedIn(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    suspend fun setLocationPermissionGranted(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOCATION_PERMISSION_GRANTED] = value
        }
    }

    suspend fun getLocationPermissionGranted(): Boolean {
        return context.dataStore.data.first()[LOCATION_PERMISSION_GRANTED] ?: false
    }
    suspend fun setShowDialog(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_DIALOG] = value
        }
    }

    suspend fun getShowDialog(): Boolean {
        return context.dataStore.data.first()[SHOW_DIALOG] ?: true // Mostrar por defecto la primera vez
    }
}