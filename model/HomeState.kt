package com.jcmateus.casanarestereo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion

@Parcelize
data class HomeState(
    var authState: EstadoAutenticacion = EstadoAutenticacion.Loading
) : Parcelable {
    fun updateAuthState(newState: EstadoAutenticacion) {
        authState = newState
    }
}
