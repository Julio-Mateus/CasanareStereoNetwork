package com.jcmateus.casanarestereo.screens.login


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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


// Define the DataStore as a top-level extension function
class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    // Keys for DataStore
    companion object {
        private const val TAG = "DataStoreManager"
        const val DATASTORE_NAME = "user_preferences"

        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val LOCATION_PERMISSION_GRANTED = booleanPreferencesKey("location_permission_granted")
        val SHOW_DIALOG = booleanPreferencesKey("show_dialog")
        val ROL_USUARIO = stringPreferencesKey("rol_usuario")
        val TERMS_ACCEPTED = booleanPreferencesKey("terms_accepted")
        val HAS_SHOWN_PRESENTATION = booleanPreferencesKey("has_shown_presentation")
        // Nueva clave para indicar si el formulario de perfil ha sido completado
        val HAS_COMPLETED_FORM = booleanPreferencesKey("has_completed_form")
        val IS_CREATING_ACCOUNT = booleanPreferencesKey("is_creating_account")

    }

    fun getIsCreatingAccount(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[IS_CREATING_ACCOUNT] ?: false // Default value: false
        }

    suspend fun saveIsCreatingAccount(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_CREATING_ACCOUNT] = value
        }
    }

    /**
     * Clears the user's role from DataStore.
     */
    suspend fun clearRol() {
        dataStore.edit { preferences ->
            preferences.remove(ROL_USUARIO)
        }
    }

    /**
     * Clears the login status from DataStore.
     */
    suspend fun clearIsLoggedIn() {
        dataStore.edit { preferences ->
            preferences.remove(IS_LOGGED_IN)
        }
    }

    /**
     * Checks if it's the first time the app is opened.
     *
     * @return A Flow emitting true if it's the first time, false otherwise.
     */
    fun isFirstTimeAppOpen(): Flow<Boolean> {
        return getTermsAccepted().map { !it }
    }

    /**
     * Saves that the app has been opened and the presentation has been shown.
     */
    suspend fun saveAppOpened() {
        saveTermsAccepted(true)
        savePresentationShown()
    }

    /**
     * Gets whether the terms have been accepted.
     *
     * @return A Flow emitting true if terms have been accepted, false otherwise.
     */
    fun getTermsAccepted(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TERMS_ACCEPTED] ?: false // Default value: false
    }

    /**
     * Saves whether the terms have been accepted.
     *
     * @param accepted True if terms have been accepted, false otherwise.
     */
    suspend fun saveTermsAccepted(accepted: Boolean) {
        dataStore.edit { preferences ->
            preferences[TERMS_ACCEPTED] = accepted
        }
    }

    /**
     * Saves that the presentation has been shown.
     */
    suspend fun savePresentationShown() {
        dataStore.edit { preferences ->
            preferences[HAS_SHOWN_PRESENTATION] = true
        }
    }

    /**
     * Gets whether the presentation has been shown.
     *
     * @return A Flow emitting true if the presentation has been shown, false otherwise.
     */
    fun getHasShownPresentation(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HAS_SHOWN_PRESENTATION] ?: false // Default value: false
    }

    /**
     * Saves the user's role.
     *
     * @param rol The user's role.
     */
    suspend fun saveRol(rol: Rol) {
        dataStore.edit { preferences ->
            preferences[ROL_USUARIO] = rol.name
        }
    }

    /**
     * Gets the user's role.
     *
     * @return A Flow emitting the user's role.
     */
    fun getRol(): Flow<Rol?> {
        return dataStore.data
            .handleDataStoreError()
            .map { preferences ->
                preferences[ROL_USUARIO]?.let {
                    Rol.valueOf(it)
                }
            }
    }

    /**
     * Saves whether the user is logged in.
     *
     * @param isLoggedIn True if the user is logged in, false otherwise.
     */
    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    /**
     * Gets whether the user is logged in.
     *
     * @return A Flow emitting true if the user is logged in, false otherwise.
     */
    fun getIsLoggedIn(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    /**
     * Saves whether location permission has been granted.
     *
     * @param value True if permission has been granted, false otherwise.
     */
    suspend fun setLocationPermissionGranted(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOCATION_PERMISSION_GRANTED] = value
        }
    }

    /**
     * Gets whether location permission has been granted.
     *
     * @return A Flow emitting true if permission has been granted, false otherwise.
     */
    fun getLocationPermissionGranted(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[LOCATION_PERMISSION_GRANTED] ?: false
        }

    /**
     * Saves whether the dialog should be shown.
     *
     * @param value True if the dialog should be shown, false otherwise.
     */
    suspend fun setShowDialog(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_DIALOG] = value
        }
    }

    /**
     * Gets whether the dialog should be shown.
     *
     * @return A Flow emitting true if the dialog should be shown, false otherwise.
     */
    fun getShowDialog(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[SHOW_DIALOG] ?: true // Show by default the first time
        }
    /**
     * Saves whether the user has completed the profile form.
     *
     * @param value True if the form has been completed, false otherwise.
     */
    suspend fun saveHasCompletedForm(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[HAS_COMPLETED_FORM] = value
        }
    }

    /**
     * Gets whether the user has completed the profile form.
     *
     * @return A Flow emitting true if the form has been completed, false otherwise.
     */
    fun getHasCompletedForm(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[HAS_COMPLETED_FORM] ?: false
        }

    /**
     * Handles DataStore errors.
     */
    private fun <T> Flow<T>.handleDataStoreError(): Flow<T> =
        catch { e ->
            if (e is IOException) {
                Log.e(TAG, "Error reading from DataStore: ${e.message}")
                emit(emptyPreferences() as T)
            } else {
                Log.e(TAG, "Unexpected error reading from DataStore: ${e.message}")
                throw e
            }
        }
}