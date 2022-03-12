package com.example.refreshlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_layout)

        swipeRefreshLayout.setOnRefreshListener {
            Log.e("happy", "good")
            swipeRefreshLayout.isRefreshing = true
            Thread.sleep(1000)
            swipeRefreshLayout.isRefreshing = false
        }
    }
}