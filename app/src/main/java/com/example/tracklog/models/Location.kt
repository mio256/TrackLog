package com.example.tracklog.models

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return "Lat: $latitude, Lon: $longitude"
    }
}