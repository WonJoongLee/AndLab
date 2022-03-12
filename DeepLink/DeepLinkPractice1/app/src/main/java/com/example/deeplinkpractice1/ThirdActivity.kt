package com.example.deeplinkpractice1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLink

@DeepLink("isaac://example/deeplink/")
class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val tvData = findViewById<TextView>(R.id.tv_data)
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val param = intent.extras
            val idString = param?.getString("id")
            tvData.text = idString ?: "null입니다"
        }
    }
}