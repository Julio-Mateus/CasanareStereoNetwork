package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import android.content.Context
import androidx.annotation.StringRes
import android.content.Intent
import com.google.android.material.datepicker.MaterialDatePicker
import java.lang.reflect.Field

fun MaterialDatePicker<*>.createIntent(context: Context): Intent {
    return Intent(context, MaterialDatePicker::class.java).apply {
        try {
            val titleTextResIdField: Field = MaterialDatePicker::class.java.getDeclaredField("titleTextResId")
            titleTextResIdField.isAccessible = true
            val titleTextResId = titleTextResIdField.getInt(this@createIntent)
            putExtra("EXTRA_TITLE_TEXT_RES_ID", titleTextResId)

            val selectionField: Field = MaterialDatePicker::class.java.getDeclaredField("selection")
            selectionField.isAccessible = true
            val selection = selectionField.get(this@createIntent)

            // Convertir selection a Long
            if (selection is Long) {
                putExtra("EXTRA_SELECTION", selection)
            } else if (selection != null) {
                putExtra("EXTRA_SELECTION", selection.toString().toLongOrNull())
            }

        } catch (e: Exception) {
            // Manejar la excepción si ocurre
            e.printStackTrace()
        }
    }
}