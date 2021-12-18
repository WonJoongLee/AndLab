package com.wonjoong.sandbox.application

import android.app.Application
import com.wonjoong.sandbox.util.PixelRatio

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initilizeSingleton()
    }

    private fun initilizeSingleton() {
        pixelRatio = PixelRatio(this)
    }

    companion object {
        lateinit var pixelRatio: PixelRatio
    }
}