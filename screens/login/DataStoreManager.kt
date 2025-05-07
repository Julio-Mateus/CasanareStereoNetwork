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

    companion object {
        private const val TAG = "DataStoreManager"
        const val DATASTORE_NAME = "user_preferences"

        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val LOCATION_PERMISSION_GRANTED = booleanPreferencesKey("location_permission_granted")
        val SHOW_DIALOG = booleanPreferencesKey("show_dialog")
        val ROL_USUARIO = stringPreferencesKey("rol_usuario")
        val TERMS_ACCEPTED = booleanPreferencesKey("terms_accepted")
        val HAS_SHOWN_PRESENTATION = booleanPreferencesKey("has_shown_presentation")
        val HAS_COMPLETED_FORM = booleanPreferencesKey("has_completed_form")
        val IS_CREATING_ACCOUNT = booleanPreferencesKey("is_creating_account")
        val IS_FIRST_TIME_APP_OPEN = booleanPreferencesKey("is_first_time_app_open")

        val USER_ID = stringPreferencesKey("user_id")
        val EMISORA_ID = stringPreferencesKey("emisora_id")
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    fun getUserId(): Flow<String?> {
        return dataStore.data
            .handleDataStoreError()
            .map { preferences ->
                preferences[USER_ID]
            }
    }
    suspend fun saveEmisoraId(emisoraId: String) {
        dataStore.edit { preferences ->
            preferences[EMISORA_ID] = emisoraId
        }
    }

    fun getEmisoraId(): Flow<String?> {
        return dataStore.data
            .handleDataStoreError()
            .map { preferences ->
                preferences[EMISORA_ID]
            }
    }

    fun getIsCreatingAccount(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[IS_CREATING_ACCOUNT] ?: false
        }

    suspend fun saveIsCreatingAccount(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_CREATING_ACCOUNT] = value
        }
    }

    suspend fun clearRol() {
        dataStore.edit { preferences ->
            preferences.remove(ROL_USUARIO)
        }
    }

    suspend fun clearIsLoggedIn() {
        dataStore.edit { preferences ->
            preferences.remove(IS_LOGGED_IN)
        }
    }

    fun isFirstTimeAppOpen(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            !(preferences[TERMS_ACCEPTED] ?: false)
        }
    }

    suspend fun saveAppOpened() {
        saveTermsAccepted(true)
        savePresentationShown()
    }

    fun getTermsAccepted(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TERMS_ACCEPTED] ?: false
    }

    suspend fun saveTermsAccepted(accepted: Boolean) {
        dataStore.edit { preferences ->
            preferences[TERMS_ACCEPTED] = accepted
        }
    }

    suspend fun savePresentationShown() {
        dataStore.edit { preferences ->
            preferences[HAS_SHOWN_PRESENTATION] = true
        }
    }

    fun getHasShownPresentation(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HAS_SHOWN_PRESENTATION] ?: false
    }

    suspend fun saveRol(rol: Rol) {
        dataStore.edit { preferences ->
            preferences[ROL_USUARIO] = rol.name
        }
    }

    fun getRol(): Flow<Rol?> {
        return dataStore.data
            .handleDataStoreError()
            .map { preferences ->
                preferences[ROL_USUARIO]?.let {
                    Rol.valueOf(it)
                }
            }
    }

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    fun getIsLoggedIn(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    suspend fun setLocationPermissionGranted(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOCATION_PERMISSION_GRANTED] = value
        }
    }

    fun getLocationPermissionGranted(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[LOCATION_PERMISSION_GRANTED] ?: false
        }

    suspend fun setShowDialog(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_DIALOG] = value
        }
    }

    fun getShowDialog(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[SHOW_DIALOG] ?: true
        }
    suspend fun saveHasCompletedForm(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[HAS_COMPLETED_FORM] = value
        }
    }

    fun getHasCompletedForm(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[HAS_COMPLETED_FORM] ?: false
        }
    fun getIsFirstTimeAppOpen(): Flow<Boolean> = dataStore.data
        .handleDataStoreError()
        .map { preferences ->
            preferences[IS_FIRST_TIME_APP_OPEN] ?: true
        }

    suspend fun setIsFirstTimeAppOpen(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME_APP_OPEN] = value
        }
    }

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