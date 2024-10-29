package com.example.tracklog.models

import java.time.LocalDateTime

data class ArrivalInformation(
    val estimatedArrivalTime: LocalDateTime,
    val currentLocation: Location,
    val goalLocation: Location,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    override fun toString(): String {
        return "Estimated arrival time: $estimatedArrivalTime, Current location: $currentLocation, Goal location: $goalLocation, Created at: $createdAt"
    }
}