package com.lutalic.geoquiz

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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
        nextButton = findViewById(getScreenOrientation(SwapButton.FORWARD))
        prevButton = findViewById(getScreenOrientation(SwapButton.BACK))
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

    private fun message(msg: Int) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT).show()
    }

    private fun checkAnswer(ans: Boolean) {
        if (ans == questions.pairs[currentIndex].second) {
            if (questions.pairs.size == 1) {
                message(R.string.win_message)
                return
            }

            Thread.sleep(800)

            message(R.string.correct_toast)
            questions.pairs.removeAt(currentIndex)
            when (currentIndex) {
                questions.pairs.size -> questionView.setText(questions.pairs[--currentIndex].first)
                else -> questionView.setText(questions.pairs[currentIndex].first)
            }
        } else {
            message(R.string.incorrect_toast)
        }
    }

    private fun swapQuestion(side: Boolean) {
        when {
            side && currentIndex < questions.pairs.size - 1 -> questionView.setText(questions.pairs[++currentIndex].first)
            !side && currentIndex > 0 -> questionView.setText(questions.pairs[--currentIndex].first)
            else -> if (side) message(R.string.last_question) else message(R.string.first_question)
        }
    }

    private fun getScreenOrientation(type: SwapButton): Int {
        return if (resources.configuration.orientation == ORIENTATION_PORTRAIT) {
           when (type) {
               SwapButton.FORWARD -> R.id.next_button
               else -> R.id.prev_button
           }
        } else {
            when (type) {
                SwapButton.FORWARD -> R.id.land_next_button
                else -> R.id.land_prev_button
            }
        }
    }
}