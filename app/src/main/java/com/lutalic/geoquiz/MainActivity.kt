package com.lutalic.geoquiz

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var falseButton: Button
    private lateinit var trueButton: Button
    private lateinit var nextButton: View
    private lateinit var prevButton: View
    private lateinit var questionView: TextView

    private val viewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.correct)
        falseButton = findViewById(R.id.incorrect)
        nextButton = findViewById(getScreenOrientation(SwapButton.FORWARD))
        prevButton = findViewById(getScreenOrientation(SwapButton.BACK))
        questionView = findViewById(R.id.question)

        questionView.setText(viewModel.currentQuestion)

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
        if (ans == viewModel.currentAnswer) {
            if (viewModel.currentSize == 1) {
                message(R.string.win_message)
                return
            }

            message(R.string.correct_toast)
            viewModel.removeElement()
            when (viewModel.currentIndex) {
                viewModel.currentSize -> {
                    viewModel.currentIndex--
                    questionView.setText(viewModel.currentQuestion)
                }
                else -> questionView.setText(viewModel.currentQuestion)
            }
        } else {
            message(R.string.incorrect_toast)
        }
    }

    private fun swapQuestion(side: Boolean) {
        when {
            side && viewModel.currentIndex < viewModel.currentSize - 1 -> {
                    viewModel.currentIndex++
                    questionView.setText(viewModel.currentQuestion)
            }
            !side && viewModel.currentIndex > 0 ->  {
                    viewModel.currentIndex--
                    questionView.setText(viewModel.currentQuestion)
            }
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