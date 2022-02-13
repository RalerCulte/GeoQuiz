package com.lutalic.geoquiz

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

private const val QUESTIONS_COUNT = 6

class FinalScreen : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_screen)

        val text: TextView = findViewById(R.id.points)
        val rightAnswers = intent.getIntExtra(INPUT_KEY, 0)

        text.text = "Your result: $rightAnswers/$QUESTIONS_COUNT"
    }
}