package com.example.deeplinkpractice1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val moveBtn = findViewById<TextView>(R.id.btn_move)
        moveBtn.setOnClickListener {
            this.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("isaac://deeplink/goToSecondActivity")
            })
        }
    }
}