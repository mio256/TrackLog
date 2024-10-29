package com.example.tracklog.services

import android.annotation.SuppressLint
import android.content.Context
import com.example.tracklog.models.ArrivalInformation
import com.example.tracklog.models.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object LocationService {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun initialize(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callback: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = Location(location.latitude, location.longitude)
                    callback(currentLocation)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun updateArrivalInformation(info: ArrivalInformation) {
        // サーバーに到着情報を送信するコードを実装
    }
}