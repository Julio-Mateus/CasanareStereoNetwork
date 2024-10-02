package com.jcmateus.casanarestereo.screens.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
sealed class EstadoAutenticacion : Parcelable {
    object LoggedIn :EstadoAutenticacion()
    object LoggedOut : EstadoAutenticacion()
    object Loading : EstadoAutenticacion()
}