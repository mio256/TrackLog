package com.example.tracklog.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.tracklog.R

class CheckInCompleteActivity : AppCompatActivity() {

    private val handler = Handler()
    private val transitionDelay: Long = 3000 // 3秒後に自動遷移

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in_complete)

        handler.postDelayed({
            // 待ち時間表示画面へ遷移
            val intent = Intent(this, WaitingTimeActivity::class.java)
            startActivity(intent)
            finish()
        }, transitionDelay)
    }
}