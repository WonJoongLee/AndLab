package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    val wordHint = Transformations.map(word) { word ->
        val randomPosition = (1..word.length).random()
        "Current word has " + word.length + " letters" +
                "\nThe letter at position " + randomPosition + " is " + word[randomPosition - 1]
            .toUpperCase()
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    private val timer: CountDownTimer

    init {
        Log.i("GameViewModel", "GameViewModel created!")

        // 초기화
        _word.value = ""
        _score.value = 0

        resetList()
        nextWord()

        // 순서대로 total time과 interval time이다.
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                // convert millis to seconds
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }
        timer.start()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    /** Method for the game completed event **/
    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel() // Memory leak을 줄이기 위해 timer를 cancel한다.
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
            //onGameFinish() // wordList가 전부 비었으면 onGameFinish() 호출
            // wordList가 모두 비었으면 timer에서 초기화를 하고 게임을 끝내는데,
            // 사용자가 다시 게임을 하고자 할 때 게임 리스트가 있어야 하므로 다시 채워 넣는다.
            resetList()
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

    companion object {
        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L
    }

}