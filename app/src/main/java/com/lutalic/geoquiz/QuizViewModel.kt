package com.lutalic.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    var currentIndex = 0
    var record = 0
    var endGame = false

    private var questions = Question(arrayListOf(
        R.string.question_australia to true,
        R.string.question_oceans to true,
        R.string.question_mideast to false,
        R.string.question_africa to false,
        R.string.question_americas to true,
        R.string.question_asia to true))

    val currentQuestion
        get() = questions.pairs[currentIndex].first

    val currentAnswer
        get() = questions.pairs[currentIndex].second

    val currentSize
        get() = questions.pairs.size

    fun removeElement() {
        questions.pairs.removeAt(currentIndex)
    }

    fun checkEnd() {
        if (endGame) {
            questions = Question(arrayListOf(R.string.end_que to true))
        }
    }
}