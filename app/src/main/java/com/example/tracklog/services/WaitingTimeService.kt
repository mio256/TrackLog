package com.example.tracklog.services

import com.example.tracklog.models.WaitingTimeInformation

object WaitingTimeService {
    fun checkWaitingTime(): WaitingTimeInformation {
        // サーバーから待ち時間情報を取得
        return WaitingTimeInformation(waitingTime = 15)
    }
}