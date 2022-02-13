package com.lutalic.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {

    private lateinit var cheatButton: Button
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        val answer = when (intent.getBooleanExtra(INPUT_KEY, false)) {
            true -> R.string.correct_button
            else -> R.string.incorrect_button
        }
        cheatButton = findViewById(R.id.cheating_but)
        text = findViewById(R.id.warning)

        cheatButton.setOnClickListener {
            text.setText(answer)
            this@CheatActivity.setResult(RESULT_OK)
        }
    }
}