package com.example.tracklog.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tracklog.R
import com.example.tracklog.services.WaitingTimeService

class WaitingTimeActivity : AppCompatActivity() {

    // UI 要素の宣言
    private lateinit var waitingTimeTextView: TextView
    private lateinit var updateButton: Button
    private lateinit var exitButton: Button

    private val handler = Handler()
    private val updateInterval: Long = 60 * 1000 // 1分ごとに更新

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_time)

        // UI 要素のバインド
        waitingTimeTextView = findViewById(R.id.waitingTimeTextView)
        updateButton = findViewById(R.id.updateButton)
        exitButton = findViewById(R.id.exitButton)

        updateWaitingTime()

        // 更新ボタンのクリックリスナー
        updateButton.setOnClickListener {
            updateWaitingTime()
        }

        // 終了ボタンのクリックリスナー
        exitButton.setOnClickListener {
            // ホーム画面（MainActivity）に戻る
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // 自動更新
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateWaitingTime()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    private fun updateWaitingTime() {
        val waitingTimeInfo = WaitingTimeService.checkWaitingTime()
        waitingTimeTextView.text = "待ち時間: ${waitingTimeInfo.waitingTime} 分"

        if (waitingTimeInfo.waitingTime <= 0) {
            // 待ち時間終了、ホーム画面へ戻る
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ハンドラーのコールバックを削除
        handler.removeCallbacksAndMessages(null)
    }
}