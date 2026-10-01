package com.techtitans.veeraconnect

import android.app.Application
import com.techtitans.veeraconnect.util.ThemeManager

class VeeraConnectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply the saved light/dark preference before any screen is drawn
        ThemeManager.applySaved(this)
    }
}
