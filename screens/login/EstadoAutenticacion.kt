package com.jcmateus.casanarestereo.screens.login

import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.parcel.Parcelize


@Parcelize
sealed class EstadoAutenticacion : Parcelable {
    data class LoggedIn(val user: FirebaseUser?, val rol: Rol?) : EstadoAutenticacion()
    object LoggedOut : EstadoAutenticacion()
    object Loading : EstadoAutenticacion()
}