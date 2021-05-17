package com.example.todolistkotlin.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.todolistkotlin.R


//extending the standard preference fragment
class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs, rootKey)
    }


}