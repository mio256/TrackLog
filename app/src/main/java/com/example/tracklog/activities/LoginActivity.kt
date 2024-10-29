package com.example.tracklog.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tracklog.databinding.ActivityLoginBinding
import com.example.tracklog.models.Driver
import com.example.tracklog.services.AuthenticationService

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root) // レイアウトの設定

        // ログインボタンのクリックリスナー
        binding.loginButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val driverID = binding.driverIdEditText.text.toString().toIntOrNull() ?: 0
            val driver = Driver(name, driverID)
            val ctx = this
            if (AuthenticationService.login(ctx, driver)) {
                // ログイン成功、メイン画面へ遷移
                startActivity(Intent(this, MainActivity::class.java))
                finish() // 現在のアクティビティを終了
            } else {
                // エラーメッセージを表示
                Toast.makeText(this, "ログインに失敗しました", Toast.LENGTH_SHORT).show()
            }
        }
    }
}