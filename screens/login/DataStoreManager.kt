package com.jcmateus.casanarestereo.screens.login


import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlin.collections.remove
import kotlin.text.get
import kotlin.text.set
import kotlin.toString


// Define the DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    // Keys for DataStore
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val LOCATION_PERMISSION_GRANTED = booleanPreferencesKey("location_permission_granted")
        val SHOW_DIALOG = booleanPreferencesKey("show_dialog")
        val ROL_USUARIO = stringPreferencesKey("rol_usuario")
        val TERMS_ACCEPTED = booleanPreferencesKey("terms_accepted")
        val HAS_SHOWN_PRESENTATION = booleanPreferencesKey("has_shown_presentation")
    }

    // Clear user role
    suspend fun clearRolUsuario() {
        dataStore.edit { preferences ->
            preferences.remove(ROL_USUARIO)
        }
    }

    // Clear logged in status
    suspend fun clearIsLoggedIn() {
        dataStore.edit { preferences ->
            preferences.remove(IS_LOGGED_IN)
        }
    }

    // Check if it's the first time the app is opened
    fun isFirstTimeAppOpen(): Flow<Boolean> {
        return getTermsAccepted().map { termsAccepted ->
            !termsAccepted // If terms are not accepted, it's the first time
        }
    }

    // Save that the app has been opened and the presentation has been shown
    suspend fun saveAppOpened() {
        saveTermsAccepted(true)
        savePresentationShown()
    }

    // Get if terms have been accepted
    fun getTermsAccepted(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TERMS_ACCEPTED] ?: false // Default value: false
    }

    // Save if terms have been accepted
    suspend fun saveTermsAccepted(accepted: Boolean) {
        dataStore.edit { preferences ->
            preferences[TERMS_ACCEPTED] = accepted
        }
    }

    // Save that the presentation has been shown
    suspend fun savePresentationShown() {
        dataStore.edit { preferences ->
            preferences[HAS_SHOWN_PRESENTATION] = true
        }
    }

    // Get if the presentation has been shown
    fun getHasShownPresentation(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HAS_SHOWN_PRESENTATION] ?: false // Default value: false
    }

    // Save user role
    suspend fun saveRolUsuario(rol: Rol) {
        dataStore.edit { preferences ->
            preferences[ROL_USUARIO] = rol.name
        }
    }

    // Get user role
    fun getRolUsuario(): Flow<Rol> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                try {
                    Rol.valueOf(preferences[ROL_USUARIO] ?: Rol.NO_DEFINIDO.name)
                } catch (e: IllegalArgumentException) {
                    Rol.NO_DEFINIDO
                }
            }
    }

    // Save if the user is logged in
    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    // Get if the user is logged in
    fun getIsLoggedIn(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    // Save if location permission is granted
    suspend fun setLocationPermissionGranted(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOCATION_PERMISSION_GRANTED] = value
        }
    }

    // Get if location permission is granted
    suspend fun getLocationPermissionGranted(): Boolean {
        return dataStore.data.first()[LOCATION_PERMISSION_GRANTED] == true
    }

    // Save if the dialog should be shown
    suspend fun setShowDialog(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_DIALOG] = value
        }
    }

    // Get if the dialog should be shown
    suspend fun getShowDialog(): Boolean {
        return dataStore.data.first()[SHOW_DIALOG] != false // Show by default the first time
    }
}