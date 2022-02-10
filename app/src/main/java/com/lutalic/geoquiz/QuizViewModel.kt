package com.lutalic.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    var currentIndex = 0

    private val questions = Question(arrayListOf(
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
}