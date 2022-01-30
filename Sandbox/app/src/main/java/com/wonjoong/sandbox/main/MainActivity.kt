package com.wonjoong.sandbox.main

import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.wonjoong.sandbox.R
import com.wonjoong.sandbox.databinding.ActivityMainBinding
import com.wonjoong.sandbox.util.BaseAppCompatActivity

class MainActivity : BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate()")
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fcv_main).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        log("onStart()")
    }

    override fun onResume() {
        super.onResume()
        log("onResume()")
    }

    override fun onPause() {
        super.onPause()
        log("onPause()")
    }

    override fun onStop() {
        super.onStop()
        log("onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        log("onRestart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy()")
    }

    private fun log(message: String) {
        Log.e(this.javaClass::getName.name, message)
    }
}