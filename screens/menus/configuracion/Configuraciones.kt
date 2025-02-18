package com.jcmateus.casanarestereo.screens.menus.configuracion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jcmateus.casanarestereo.screens.home.Destinos

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Configuraciones(innerPadding: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Configuraciones",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sección de General
        ConfiguracionSection(title = "General") {
            ConfiguracionItem(
                icon = Icons.Filled.Palette,
                title = "Tema",
                description = "Cambiar el tema de la aplicación",
                onClick = {
                    navController.navigate(Destinos.Tema.ruta)
                }
            )
            ConfiguracionItem(
                icon = Icons.Filled.Notifications,
                title = "Notificaciones",
                description = "Gestionar las notificaciones",
                onClick = {
                    // TODO: Implementar gestión de notificaciones
                    navController.navigate(Destinos.Notificaciones.ruta)
                }
            )
        }

        // Sección de Cuenta
        ConfiguracionSection(title = "Cuenta") {
            ConfiguracionItem(
                icon = Icons.Filled.Lock,
                title = "Privacidad",
                description = "Configurar la privacidad de tu cuenta",
                onClick = {
                    // TODO: Implementar configuración de privacidad
                    navController.navigate(Destinos.Privacidad.ruta)
                }
            )
            ConfiguracionItem(
                icon = Icons.Filled.Info,
                title = "Acerca de",
                description = "Información sobre la aplicación",
                onClick = {
                    // TODO: Implementar pantalla 'Acerca de'
                    navController.navigate(Destinos.AcercaDe.ruta)
                }
            )
        }
    }
}

@Composable
fun ConfiguracionSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun ConfiguracionItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

// Funciones para mostrar los Dialogs
@Composable
fun ShowThemeDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Cambiar Tema") },
            text = { Text("Aquí puedes cambiar el tema de la aplicación.") },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun ShowNotificationsDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Gestionar Notificaciones") },
            text = { Text("Aquí puedes gestionar las notificaciones de la aplicación.") },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun ShowPrivacyDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Configurar Privacidad") },
            text = { Text("Aquí puedes configurar la privacidad de tu cuenta.") },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun ShowAboutDialog(showDialog: Boolean, onDismiss: () -> Unit, context: Context) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Acerca de") },
            text = { Text("Aquí puedes ver la información sobre la aplicación.") },
            confirmButton = {
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
                    context.startActivity(intent)
                    onDismiss()
                }) {
                    Text("Ir a Google")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        )
    }
}