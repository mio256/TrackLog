package com.example.tracklog.models

import java.time.LocalDateTime

data class CheckInInformation(
    val checkInTime: LocalDateTime = LocalDateTime.now(),
    val driverID: Int
) {
    override fun toString(): String {
        return "Check-in time: $checkInTime, Driver ID: $driverID"
    }
}