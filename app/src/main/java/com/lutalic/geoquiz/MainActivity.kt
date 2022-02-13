package com.lutalic.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModelProvider

const val INPUT_KEY = "com.lutalic.geoquiz.input_key"
private const val KEY_INDEX = "index"
private const val FINAL = "end"

class MainActivity : AppCompatActivity() {

    private lateinit var falseButton: Button
    private lateinit var trueButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: View
    private lateinit var prevButton: View
    private lateinit var questionView: TextView

    private var isCheater: Boolean = false

    private val getContent = registerForActivityResult(ResultActivity()) {
        if (it!!) {
            isCheater = true
        }
    }

    private val viewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, viewModel.currentIndex)
        savedInstanceState.putBoolean(FINAL, viewModel.endGame)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        viewModel.endGame = savedInstanceState?.getBoolean(FINAL, false) ?: false

        viewModel.checkEnd()

        trueButton = findViewById(R.id.correct)
        falseButton = findViewById(R.id.incorrect)
        cheatButton = findViewById(R.id.cheat_button)
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

        cheatButton.setOnClickListener {
            if (viewModel.endGame) {
                return@setOnClickListener
            }
            getContent.launch(viewModel.currentAnswer)
        }
    }

    private fun message(msg: Int) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT).show()
    }

    private fun checkAnswer(ans: Boolean) {
        if (viewModel.endGame) {
            return
        }

        if (ans == viewModel.currentAnswer) {
            if (viewModel.currentSize == 1) {
                viewModel.endGame = true
                startActivity(Intent(this, FinalScreen::class.java)
                    .putExtra(INPUT_KEY, viewModel.record))
                questionView.setText(R.string.end_que)
                return
            }

            if (isCheater) {
                message(R.string.judgment_toast)
                isCheater = false
            } else {
                viewModel.record++
                message(R.string.correct_toast)
            }
        } else {
            message(R.string.incorrect_toast)
        }

        updateQuestion()
    }

    private fun swapQuestion(side: Boolean) {
        if (viewModel.endGame) {
            return
        }

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

    private fun updateQuestion() {
        viewModel.removeElement()
        when (viewModel.currentIndex) {
            viewModel.currentSize -> {
                viewModel.currentIndex--
                questionView.setText(viewModel.currentQuestion)
            }
            else -> questionView.setText(viewModel.currentQuestion)
        }
    }

    private class ResultActivity : ActivityResultContract<Boolean, Boolean?>() {
        override fun createIntent(context: Context, input: Boolean): Intent {
            return Intent(context, CheatActivity::class.java)
                .putExtra(INPUT_KEY, input)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }
}