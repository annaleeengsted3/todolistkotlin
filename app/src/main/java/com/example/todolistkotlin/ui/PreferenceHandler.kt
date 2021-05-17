package com.example.todolistkotlin.ui

import android.content.Context
import androidx.preference.PreferenceManager

//Singleton class to handle preferences from any fragment/activity
object PreferenceHandler {
    private const val SETTINGS_THEMEKEY = "night"
    fun isNightMode(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_THEMEKEY, false)
    }
}

