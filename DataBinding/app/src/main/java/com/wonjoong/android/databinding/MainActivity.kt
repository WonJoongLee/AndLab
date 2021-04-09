package com.wonjoong.android.databinding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wonjoong.android.databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val myName : MyName = MyName("WonJoong")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btTemp.setOnClickListener {
            Toast.makeText(this, "hello world!", Toast.LENGTH_SHORT).show()
        }
        binding.myName = myName

        binding.btTemp.setOnClickListener {
            changeText()
        }
    }

    private fun changeText() {
        binding.apply {
            myName?.nickname = "changed Nickname"
            invalidateAll()
        }
    }

    /**
     * Click handler for the Done button.
     * Demonstrates how data binding can be used to make code much more readable
     * by eliminating calls to findViewById and changing data in the binding object.
     */
    // https://github.com/google-developer-training/android-kotlin-fundamentals-apps/tree/master/AboutMeDataBinding
    // 위 주소 코드, 내 앱에서는 따로 작동하지는 않는데 binding.apply 부분을 확인해보기 위해 추가함.
    // invalidateAll()함수를 통해 update된 값으로 UI를 refresh 할 수 있음
    private fun addNickname(view: View) {
        binding. apply {
            //myName?.nickname = nicknameEdit.text.toString()
            invalidateAll()
            //nicknameEdit.visibility = View.GONE
            //doneButton.visibility = View.GONE
            //nicknameText.visibility = View.VISIBLE
        }

        // Hide the keyboard.
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}