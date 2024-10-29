package com.example.tracklog.models

data class WaitingTimeInformation(
    val waitingTime: Int // 分単位
) {
    override fun toString(): String {
        return "Waiting time: $waitingTime"
    }
}