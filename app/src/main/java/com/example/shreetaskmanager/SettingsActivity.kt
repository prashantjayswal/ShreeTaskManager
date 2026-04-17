package com.example.shreetaskmanager

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUtils.setupBottomNavigation(this, navView, 0) // Settings isn't in menu yet but we handle click

        val sharedPref = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        
        val switchLock = findViewById<SwitchMaterial>(R.id.switchAppLock)
        switchLock.isChecked = sharedPref.getBoolean("lock_enabled", false)
        switchLock.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("lock_enabled", isChecked).apply()
        }

        val switchAlarm = findViewById<SwitchMaterial>(R.id.switchAlarmReminders)
        switchAlarm.isChecked = sharedPref.getBoolean("alarm_enabled", false)
        switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("alarm_enabled", isChecked).apply()
        }
    }
}
