package com.example.tracklog.services

import com.example.tracklog.models.CheckInInformation
import com.example.tracklog.models.Driver

object CheckInService {
    fun automaticCheckIn(driver: Driver): CheckInInformation {
        val checkInInfo = CheckInInformation(driverID = driver.driverID)
        // サーバーにチェックイン情報を送信
        return checkInInfo
    }
}