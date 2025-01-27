package com.jcmateus.casanarestereo.screens.login

import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.parcel.Parcelize


sealed class EstadoAutenticacion : Parcelable {
    @Parcelize
    data class LoggedIn(val user: FirebaseUser?, val rol: Rol?) : EstadoAutenticacion()

    @Parcelize
    data class LoggedInWithPendingRol(val user: FirebaseUser?) : EstadoAutenticacion()

    @Parcelize
    object LoggedOut : EstadoAutenticacion()

    @Parcelize
    object Loading : EstadoAutenticacion()

    @Parcelize
    data class Error(val message: String) : EstadoAutenticacion()
}