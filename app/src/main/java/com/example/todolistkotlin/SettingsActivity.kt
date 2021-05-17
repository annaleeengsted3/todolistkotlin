package com.example.todolistkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistkotlin.ui.PreferenceHandler
import com.example.todolistkotlin.ui.PreferencesFragment


class SettingsActivity : AppCompatActivity() {
    private var isNight: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        isNight = PreferenceHandler.isNightMode(this)
        if(isNight) {
            setTheme(R.style.nightTheme)
        }
        super.onCreate(savedInstanceState)


        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, PreferencesFragment())
                .commit()
    }
}
