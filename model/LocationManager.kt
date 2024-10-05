package com.jcmateus.casanarestereo.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LocationManager(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val geoFire = GeoFire(FirebaseFirestore.getInstance().collection("emisoras"))
    private val emisoraDao: EmisoraDao
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
        emisoraDao = db.emisoraDao()
    }

    fun obtenerEmisorasCercanas(radio: Double, callback: (List<Emisora>) -> Unit) {
        scope.launch {
            // Intentar obtener las emisoras de la caché
            val emisorasCache = emisoraDao.obtenerTodasLasEmisoras().firstOrNull()
            if (emisorasCache != null && emisorasCache.isNotEmpty()) {
                // Devolver las emisoras de la caché
                callback(emisorasCache.map { it.toEmisora() }) // Convertir a Emisora
                return@launch
            }

            // Obtener la ubicación del usuario
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@launch
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val geoQuery = geoFire.queryAtLocation(
                        GeoLocation(location.latitude, location.longitude),
                        radio
                    )
                    geoQuery.addGeoQueryEventListener { key, location: GeoLocation? ->
                        // Obtener la información de la emisora con el ID key
                        FirebaseFirestore.getInstance().collection("emisoras").document(key)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    val emisora = document.toObject(Emisora::class.java)
                                    // Agregar la emisora a la lista de emisoras cercanas
                                    val emisorasCercanas = mutableListOf<Emisora>()
                                    emisora?.let { emisorasCercanas.add(it) }
                                    // Insertar las emisoras en la caché
                                    scope.launch {
                                        emisoraDao.insertarEmisoras(emisorasCercanas)
                                    }
                                    // Llamar al callback con la lista de emisoras cercanas
                                    callback(emisorasCercanas)
                                }
                            }
                    }
                } else {
                    // Manejar el caso en que no se pueda obtener la ubicación
                    // ...
                }
            }
        }
    }

    // Otros métodos para obtener noticias, podcasts, etc. según la ubicación
    // ...
}