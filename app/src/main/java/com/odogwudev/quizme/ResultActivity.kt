package com.odogwudev.quizme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    private lateinit var tvName: TextView
    private lateinit var tvScore: TextView
    private lateinit var btFinish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initViews()
    }

    private fun initViews() {
        tvName = findViewById(R.id.tvName)
        tvName.text = intent.getStringExtra(Constants.USER_NAME)

        tvScore = findViewById(R.id.tvScore)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        tvScore.text = getString(R.string.result_score, correctAnswers, totalQuestions)

        btFinish = findViewById(R.id.btFinish)

        btFinish.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}