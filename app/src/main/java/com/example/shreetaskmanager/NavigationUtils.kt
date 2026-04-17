package com.example.shreetaskmanager

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavigationUtils {
    fun setupBottomNavigation(activity: Activity, navView: BottomNavigationView, selectedItemId: Int) {
        navView.selectedItemId = selectedItemId
        navView.setOnItemSelectedListener { item ->
            if (item.itemId == selectedItemId) return@setOnItemSelectedListener true
            
            when (item.itemId) {
                R.id.nav_home -> {
                    activity.startActivity(Intent(activity, HomeActivity::class.java))
                    activity.overridePendingTransition(0, 0)
                    activity.finish()
                    true
                }
                R.id.nav_calendar -> {
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.overridePendingTransition(0, 0)
                    activity.finish()
                    true
                }
                R.id.nav_analysis -> {
                    activity.startActivity(Intent(activity, AnalysisActivity::class.java))
                    activity.overridePendingTransition(0, 0)
                    activity.finish()
                    true
                }
                R.id.nav_tasks -> {
                    activity.startActivity(Intent(activity, AllTasksActivity::class.java))
                    activity.overridePendingTransition(0, 0)
                    activity.finish()
                    true
                }
                R.id.nav_settings -> {
                    activity.startActivity(Intent(activity, SettingsActivity::class.java))
                    activity.overridePendingTransition(0, 0)
                    activity.finish()
                    true
                }
                else -> false
            }
        }
    }
}
