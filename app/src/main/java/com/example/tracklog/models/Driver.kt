package com.example.tracklog.models

data class Driver(
    val name: String,
    val driverID: Int
) {
    override fun toString(): String {
        return "Name: $name, Driver ID: $driverID"
    }
}