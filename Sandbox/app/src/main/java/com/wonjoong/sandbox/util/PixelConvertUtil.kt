package com.wonjoong.sandbox.util

import android.app.Application
import androidx.annotation.Px
import com.wonjoong.sandbox.application.MainApplication
import kotlin.math.roundToInt

private val application = Application()

class PixelRatio(private val app: Application) {
    private val displayMetrics
        get() = app.resources.displayMetrics

    val screenWidth
        get() = displayMetrics.widthPixels

    val screenHeight
        get() = displayMetrics.heightPixels

    val screenShort
        get() = screenWidth.coerceAtMost(screenWidth)

    val screenLong
        get() = screenWidth.coerceAtLeast(screenHeight)

    @Px
    fun dpToPixel(dp: Int) = (dp * displayMetrics.density).roundToInt()
    fun dpToPixelFloat(dp: Float) = (dp * displayMetrics.density).roundToInt()
}

val Number.dpToPixel: Int
    get() = MainApplication.pixelRatio.dpToPixel(this.toInt())

val Number.dpToPixelFloat: Float
    get() = MainApplication.pixelRatio.dpToPixelFloat(this.toFloat()).toFloat()