package com.jcmateus.casanarestereo.model

import android.os.Parcelable
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion

data class User(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String,
    val isFirstTime: Boolean = true,
    val rol: String

){
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "id" to id,
            "userId" to userId,
            "displayName" to displayName,
            "avatarUrl" to avatarUrl,
            "quote" to quote,
            "profession" to profession,
            "isFirstTime" to isFirstTime,
            "rol" to rol
        )
    }
}
