package com.techtitans.veeraconnect.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/** Saves and applies the light/dark preference (user story 4: accessibility). */
object ThemeManager {
    private const val PREFS = "veera_prefs"
    private const val KEY_DARK = "dark_mode"

    fun isDark(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_DARK, false)

    fun setDark(context: Context, dark: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_DARK, dark).apply()
        apply(dark)
    }

    fun applySaved(context: Context) = apply(isDark(context))

    private fun apply(dark: Boolean) = AppCompatDelegate.setDefaultNightMode(
        if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    )
}
