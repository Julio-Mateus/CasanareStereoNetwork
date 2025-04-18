package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jcmateus.casanarestereo.R

@Composable
fun Clasificados(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Clasificados",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(getClasificados()) { clasificado ->
                ClasificadoCard(clasificado = clasificado)
            }
        }
    }
}

@Composable
fun ClasificadoCard(clasificado: Clasificado) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = clasificado.imagen),
                    contentDescription = "Imagen del clasificado ${clasificado.titulo}",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = clasificado.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (expanded) {
                        Text(
                            text = clasificado.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) "Mostrar menos" else "Mostrar más"
                    )
                }
            }
            if (expanded) {
                Text(
                    text = clasificado.contacto,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// Data class para representar un clasificado
data class Clasificado(
    val titulo: String,
    val descripcion: String,
    val contacto: String,
    val imagen: Int
)

// Función para obtener una lista de clasificados (reemplaza con tu lógica real)
fun getClasificados(): List<Clasificado> {
    return listOf(
        Clasificado(
            "Clasificado 1",
            "Descripción del clasificado 1. Esta es una descripción más larga para mostrar el efecto de expansión. Aquí puedes poner más detalles sobre el clasificado.",
            "Contacto: 123-456-7890",
            R.drawable.carru1
        ),
        Clasificado(
            "Clasificado 2",
            "Descripción del clasificado 2. Esta es una descripción más larga para mostrar el efecto de expansión. Aquí puedes poner más detalles sobre el clasificado.",
            "Contacto: 987-654-3210",
            R.drawable.carru2
        ),
        Clasificado(
            "Clasificado 3",
            "Descripción del clasificado 3. Esta es una descripción más larga para mostrar el efecto de expansión. Aquí puedes poner más detalles sobre el clasificado.",
            "Contacto: 555-123-4567",
            R.drawable.carru3
        ),
        Clasificado(
            "Clasificado 4",
            "Descripción del clasificado 4. Esta es una descripción más larga para mostrar el efecto de expansión. Aquí puedes poner más detalles sobre el clasificado.",
            "Contacto: 555-123-4567",
            R.drawable.carru1
        ),
        Clasificado(
            "Clasificado 5",
            "Descripción del clasificado 5. Esta es una descripción más larga para mostrar el efecto de expansión. Aquí puedes poner más detalles sobre el clasificado.",
            "Contacto: 555-123-4567",
            R.drawable.carru2
        )
    )
}