package com.peter.landing.ui.util

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

enum class ThemeMode {
    LIGHT, DARK, DEFAULT
}

fun setThemeMode(mode: ThemeMode) {
    when (mode) {
        ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        ThemeMode.DEFAULT -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }
    }
}