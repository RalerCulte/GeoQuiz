package com.lutalic.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
//import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var falseButton: Button
    private lateinit var trueButton: Button
    private lateinit var nextButton: View
    private lateinit var prevButton: View
    private lateinit var questionView: TextView
    private var currentIndex = 0

    private val questions = Question(arrayListOf(
        R.string.question_australia to true,
        R.string.question_oceans to true,
        R.string.question_mideast to false,
        R.string.question_africa to false,
        R.string.question_americas to true,
        R.string.question_asia to true))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.correct)
        falseButton = findViewById(R.id.incorrect)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionView = findViewById(R.id.question)

        // в setOnClickListener необходимо передать лямбду с обработкой ивента
        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            swapQuestion(true)
        }

        prevButton.setOnClickListener {
            swapQuestion(false)
        }
    }

    private fun checkAnswer(ans: Boolean) {
        val answer: Int

        if (ans == questions.pairs[currentIndex].second) {
            answer = if (questions.pairs.size == 1) {
                R.string.win_message
            } else {
                R.string.correct_toast
            }

            questions.pairs.removeAt(currentIndex)
            if (questions.pairs.size != 0) {
                when (currentIndex) {
                    questions.pairs.size - 1 -> questionView.setText(questions.pairs[currentIndex - 1].first)
                    else -> questionView.setText(questions.pairs[currentIndex + 1].first)
                }
            }
        } else {
            answer = R.string.incorrect_toast
        }

        Toast.makeText(
            this,
            answer,
            Toast.LENGTH_SHORT).show()
    }

    private fun swapQuestion(side: Boolean) {
        when {
            side && currentIndex < questions.pairs.size - 1 -> questionView.setText(questions.pairs[++currentIndex].first)
            !side && currentIndex > 0 -> questionView.setText(questions.pairs[--currentIndex].first)
            else -> {
                val mes = if (side) R.string.last_question else R.string.first_question
                Toast.makeText(
                this,
                mes,
                Toast.LENGTH_SHORT).show()
            }
        }
    }
}