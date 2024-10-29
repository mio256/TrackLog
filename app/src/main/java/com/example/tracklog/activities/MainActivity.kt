package com.example.tracklog.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tracklog.R

class MainActivity : AppCompatActivity() {

    private lateinit var arrivalInfoButton: Button
    private lateinit var surveyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrivalInfoButton = findViewById(R.id.arrivalInfoButton)
        surveyButton = findViewById(R.id.surveyButton)

        arrivalInfoButton.setOnClickListener {
            val intent = Intent(this, ArrivalInfoActivity::class.java)
            startActivity(intent)
        }

        surveyButton.setOnClickListener {
            val intent = Intent(this, SurveyActivity::class.java)
            startActivity(intent)
        }
    }
}