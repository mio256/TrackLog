package com.example.tracklog.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tracklog.R
import com.example.tracklog.models.ArrivalInformation
import com.example.tracklog.models.Driver
import com.example.tracklog.models.Location
import com.example.tracklog.services.CheckInService
import com.example.tracklog.services.LocationService
import java.time.LocalDateTime

class ArrivalInfoActivity : AppCompatActivity() {

    // UI要素の宣言
    private lateinit var estimatedArrivalTimeTextView: TextView
    private lateinit var currentLocationTextView: TextView
    private lateinit var destinationLatitudeEditText: EditText
    private lateinit var destinationLongitudeEditText: EditText
    private lateinit var setDestinationButton: Button

    private val handler = Handler()
    private val updateInterval: Long = 60 * 1000 // 1分ごとに更新

    private var destinationLocation: Location? = null

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val CHECK_IN_DISTANCE_THRESHOLD = 0.5 // 単位：km
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrival_info)

        // UI要素のバインド
        estimatedArrivalTimeTextView = findViewById(R.id.estimatedArrivalTimeTextView)
        currentLocationTextView = findViewById(R.id.currentLocationTextView)
        destinationLatitudeEditText = findViewById(R.id.destinationLatitudeEditText)
        destinationLongitudeEditText = findViewById(R.id.destinationLongitudeEditText)
        setDestinationButton = findViewById(R.id.setDestinationButton)

        // LocationServiceを初期化
        LocationService.initialize(this)

        // 位置情報パーミッションの確認とリクエスト
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
        }

        // ボタンのクリックリスナーを設定
        setDestinationButton.setOnClickListener {
            val latitudeText = destinationLatitudeEditText.text.toString()
            val longitudeText = destinationLongitudeEditText.text.toString()

            if (latitudeText.isNotEmpty() && longitudeText.isNotEmpty()) {
                val latitude = latitudeText.toDoubleOrNull()
                val longitude = longitudeText.toDoubleOrNull()

                if (latitude != null && longitude != null) {
                    destinationLocation = Location(latitude, longitude)
                    Toast.makeText(this, "目的地を設定しました", Toast.LENGTH_SHORT).show()
                    // 目的地を設定したら、位置情報の更新を開始
                    startLocationUpdates()
                } else {
                    Toast.makeText(this, "有効な座標を入力してください", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "座標を入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLocationUpdates() {
        // 既に位置情報の更新が始まっている場合は停止
        handler.removeCallbacksAndMessages(null)
        updateLocationAndTime()
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateLocationAndTime()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    private fun updateLocationAndTime() {
        // 目的地が設定されていない場合は更新しない
        val destination = destinationLocation ?: return

        LocationService.getCurrentLocation { currentLocation ->
            if (currentLocation != null) {
                // 現在地の表示を更新
                runOnUiThread {
                    val latitude = currentLocation.latitude
                    val longitude = currentLocation.longitude
                    currentLocationTextView.text = "現在地: 緯度 $latitude, 経度 $longitude"
                }

                val estimatedTime = calculateEstimatedArrivalTime(currentLocation, destination)
                runOnUiThread {
                    estimatedArrivalTimeTextView.text = "到着予定時刻: $estimatedTime"
                }

                // 到着情報の更新（サーバーへの送信など）
                val arrivalInfo = ArrivalInformation(
                    estimatedArrivalTime = estimatedTime,
                    currentLocation = currentLocation,
                    goalLocation = destination,
                    createdAt = LocalDateTime.now()
                )
                LocationService.updateArrivalInformation(arrivalInfo)

                // 自動チェックインの開始
                checkProximityForCheckIn(currentLocation, destination)
            } else {
                runOnUiThread {
                    Toast.makeText(this, "位置情報を取得できませんでした", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun calculateEstimatedArrivalTime(
        currentLocation: Location,
        destination: Location
    ): LocalDateTime {
        // 距離を計算（簡易的に直線距離を使用）
        val distance = calculateDistance(currentLocation, destination)
        // 平均速度を設定（例：60km/h）
        val averageSpeed = 60.0 // km/h
        // 到着までの時間を計算
        val timeInHours = distance / averageSpeed
        val timeInMinutes = (timeInHours * 60).toLong()

        // 到着予定時刻を計算
        return LocalDateTime.now().plusMinutes(timeInMinutes)
    }

    private fun calculateDistance(loc1: Location, loc2: Location): Double {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            loc1.latitude,
            loc1.longitude,
            loc2.latitude,
            loc2.longitude,
            results
        )
        // メートルからキロメートルに変換
        return results[0] / 1000.0
    }

    private fun checkProximityForCheckIn(currentLocation: Location, destination: Location) {
        val distanceToDestination = calculateDistance(currentLocation, destination)
        if (distanceToDestination <= CHECK_IN_DISTANCE_THRESHOLD) {
            // チェックイン処理を開始
            startCheckInProcess()
        }
    }

    private fun startCheckInProcess() {
        val driver = getLoggedInDriver()
        val checkInInfo = CheckInService.automaticCheckIn(driver)
        // チェックイン成功後、チェックイン完了画面へ遷移
        runOnUiThread {
            Toast.makeText(this, "チェックインが完了しました", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CheckInCompleteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getLoggedInDriver(): Driver {
        // ログイン時に保存したドライバー情報を取得
        // ここではダミーのデータを返します
        return Driver("山田太郎", 12345)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // パーミッションが許可された場合、何もしない
                // 目的地を設定したら位置情報の更新を開始します
            } else {
                // パーミッションが拒否された場合、メッセージを表示
                Toast.makeText(this, "位置情報のパーミッションが必要です", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ハンドラーのコールバックを削除
        handler.removeCallbacksAndMessages(null)
    }
}