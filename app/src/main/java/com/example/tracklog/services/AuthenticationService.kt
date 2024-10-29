package com.example.tracklog.services

import android.content.Context
import com.example.tracklog.models.Driver

object AuthenticationService {
    private const val PREFS_NAME = "DriverPrefs"
    private const val KEY_NAME = "driver_name"
    private const val KEY_ID = "driver_id"

    fun login(context: Context, driver: Driver): Boolean {
        // モックとして、常にログイン成功とする
        saveDriverInfo(context, driver)
        return true
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_NAME) && prefs.contains(KEY_ID)
    }

    fun getLoggedInDriver(context: Context): Driver? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_NAME, null)
        val id = prefs.getInt(KEY_ID, -1)
        return if (name != null && id != -1) {
            Driver(name, id)
        } else {
            null
        }
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    private fun saveDriverInfo(context: Context, driver: Driver) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_NAME, driver.name)
            putInt(KEY_ID, driver.driverID)
            apply()
        }
    }
}