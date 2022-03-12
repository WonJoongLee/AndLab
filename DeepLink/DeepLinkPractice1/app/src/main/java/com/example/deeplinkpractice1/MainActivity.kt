package com.example.deeplinkpractice1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val moveBtn = findViewById<Button>(R.id.btn_move)
        val moveToThird = findViewById<Button>(R.id.btn_move_to_third)
        moveBtn.setOnClickListener {
            this.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("isaac://deeplink/goToSecondActivity")
            })
        }
        moveToThird.setOnClickListener {
            this.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("isaac://example/deeplink")
            })
        }
    }
}