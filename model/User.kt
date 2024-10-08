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
        return mutableMapOf(
            "user_Id" to this.userId,
            "display_Name" to this.displayName,
            "avatar_url" to this.avatarUrl,
            "quote" to this.quote,
            "profession" to this.profession,
            "isFirstTime" to this.isFirstTime,
            "rol" to this.rol
        )
    }
}
