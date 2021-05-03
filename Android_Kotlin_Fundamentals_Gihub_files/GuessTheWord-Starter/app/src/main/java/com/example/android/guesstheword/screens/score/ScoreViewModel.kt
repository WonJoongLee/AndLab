package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ScoreViewModel(finalScore: Int) : ViewModel() {
    // 최종 점수
    val score = finalScore

    class ScoreViewModelFactory(private val finalScore: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            // 새로 생성된 ScoreViewModel object를 반환한다.
            if(modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
                return ScoreViewModel(finalScore) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

    init {
        Log.i("ScoreViewModel", "Final score is $finalScore")
    }
}