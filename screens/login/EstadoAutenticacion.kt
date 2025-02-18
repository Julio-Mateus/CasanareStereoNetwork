package com.jcmateus.casanarestereo.screens.login

import android.os.Parcelable
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import kotlinx.parcelize.Parcelize


sealed class EstadoAutenticacion : Parcelable {
    @Parcelize
    data class LoggedIn(val rol: Rol?) : EstadoAutenticacion() {
        init {
            Log.d("EstadoAutenticacion", "Estado: LoggedIn, Rol: $rol")
        }
    }

    @Parcelize
    data class LoggedInWithPendingRol(val user: FirebaseUser?) : EstadoAutenticacion() {
        init {
            Log.d("EstadoAutenticacion", "Estado: LoggedInWithPendingRol, User: ${user?.uid}")
        }
    }

    @Parcelize
    object LoggedOut : EstadoAutenticacion() {
        init {
            Log.d("EstadoAutenticacion", "Estado: LoggedOut")
        }
    }

    @Parcelize
    object Loading : EstadoAutenticacion() {
        init {
            Log.d("EstadoAutenticacion", "Estado: Loading")
        }
    }

    @Parcelize
    data class Error(val message: String) : EstadoAutenticacion() {
        init {
            Log.e("EstadoAutenticacion", "Estado: Error, Message: $message")
        }
    }
}