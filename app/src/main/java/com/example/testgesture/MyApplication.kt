package com.example.testgesture

import android.app.Application
import io.hotplane.hotplane.HotPlane

class MyApplication : Application() {
    override fun onCreate() {
        HotPlane.setAccessCode("abcde", this)

        super.onCreate()
    }
}