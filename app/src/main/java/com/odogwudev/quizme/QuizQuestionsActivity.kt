package com.odogwudev.quizme

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private var mCurrentPosition: Int = 1
    private lateinit var mQuestionsList: ArrayList<Question>
    private var mSelectedOptionPosition: Int = 0
    private lateinit var pb: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var ivFlag: ImageView
    private lateinit var tvOptionOne: TextView
    private lateinit var tvOptionTwo: TextView
    private lateinit var tvOptionThree: TextView
    private lateinit var tvOptionFour: TextView
    private lateinit var btSubmit: Button
    private var mCorrectAnswers = 0
    private var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        initViews()

        setClickEvents()

        setQuestion()
    }

    private fun initViews() {
        pb = findViewById(R.id.pb)
        tvProgress = findViewById(R.id.tvProgress)
        tvQuestion = findViewById(R.id.tvQuestion)
        ivFlag = findViewById(R.id.ivFlag)
        tvOptionOne = findViewById(R.id.tvOptionOne)
        tvOptionTwo = findViewById(R.id.tvOptionTwo)
        tvOptionThree = findViewById(R.id.tvOptionThree)
        tvOptionFour = findViewById(R.id.tvOptionFour)
        btSubmit = findViewById(R.id.btSubmit)

        mUserName = intent.getStringExtra(Constants.USER_NAME)
    }

    private fun setQuestion() {
        mQuestionsList = Constants.getQuestions()

        val question = mQuestionsList[mCurrentPosition - 1]

        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList.size) {
            btSubmit.text = "FINISH"
        } else {
            btSubmit.text = "SUBMIT"
        }

        pb.progress = mCurrentPosition

        tvProgress.text = "$mCurrentPosition/${pb.max}"

        tvQuestion.text = question.text

        ivFlag.setImageResource(question.image)

        tvOptionOne.text = question.optionOne
        tvOptionTwo.text = question.optionTwo
        tvOptionThree.text = question.optionThree
        tvOptionFour.text = question.optionFour
    }

    private fun setClickEvents() {
        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)
        btSubmit.setOnClickListener(this)
    }

    private fun selectedOptionView(tv: TextView, selectedOptNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptNum

        tv.typeface = Typeface.DEFAULT_BOLD
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(tvOptionOne)
        options.add(tvOptionTwo)
        options.add(tvOptionThree)
        options.add(tvOptionFour)

        for (op in options) {
            op.typeface = Typeface.DEFAULT
            op.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when(answer) {
            1 -> tvOptionOne.background = ContextCompat.getDrawable(this, drawableView)
            2 -> tvOptionTwo.background = ContextCompat.getDrawable(this, drawableView)
            3 -> tvOptionThree.background = ContextCompat.getDrawable(this, drawableView)
            4 -> tvOptionFour.background = ContextCompat.getDrawable(this, drawableView)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tvOptionOne -> selectedOptionView(tvOptionOne, 1)
            R.id.tvOptionTwo -> selectedOptionView(tvOptionTwo, 2)
            R.id.tvOptionThree -> selectedOptionView(tvOptionThree, 3)
            R.id.tvOptionFour -> selectedOptionView(tvOptionFour, 4)
            R.id.btSubmit -> {
                if(mSelectedOptionPosition == 0) {
                    mCurrentPosition++

                    if(mCurrentPosition <= mQuestionsList.size) {
                        setQuestion()
                    } else {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(Constants.USER_NAME, mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                        intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList.size)

                        startActivity(intent)
                        finish()
                    }
                } else {
                    val question = mQuestionsList[mCurrentPosition - 1]
                    if(question.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.incorrect_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if(mCurrentPosition == mQuestionsList.size)
                        btSubmit.text = "FINISH"
                    else
                        btSubmit.text = "GO TO THE NEXT QUESTION"

                    mSelectedOptionPosition = 0
                }
            }
        }
    }
}