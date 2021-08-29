package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    // backing property 적용
    private val _word = MutableLiveData<String>() // Live Data type으로 바꿈.
    val word: LiveData<String>
        get() = _word

    // The current score
    // backing property 적용
    private val _score = MutableLiveData<Int>() // Live Data type으로 바꿈.
    val score: LiveData<Int>
        get() = _score

    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created!")

        // 초기화
        _word.value = ""
        _score.value = 0

        resetList()
        nextWord()
    }

    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

    /** Method for the game completed event **/
    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            onGameFinish() // wordList가 전부 비었으면 onGameFinish() 호출
        } else {
            //Select and remove a word from the list
            //word = wordList.removeAt(0)
            _word.value = wordList.removeAt(0)
        }
        //updateWordText() 이 두 함수는 UI update이기 때문에 GameFragment에서 호출되어야 한다.
        //updateScoreText()
    }

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

}