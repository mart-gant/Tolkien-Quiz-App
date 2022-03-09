package com.martgant.tolkienquizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.StringRes
import com.martgant.tolkienquizapp.base.BaseFragment
import com.martgant.tolkienquizapp.databinding.FragmentQuestionBinding
import kotlin.random.Random

class QuestionFragment : BaseFragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    
    private companion object {
        const val QUESTION_FORMAT = "question_%d"
        const val ANSWER_CORRECT_FORMAT = "question_%d_answer_correct"
        const val ANSWER_INCORRECT_FORMAT = "question_%d_answer_incorrect_%d"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh()
    }

    private fun refresh() {
        val (questionRes, answers) = getQuestionProps()
        binding.questionDescription.text = context?.getString(questionRes)
        val buttons = listOf(binding.answer1, binding.answer2)
        buttons.forEachIndexed { index, button ->
            bindButton(button, answers[index])
        }
    }

    private fun bindButton(button: Button, answer: QuestionProps.Answer) {
        button.text = context?.getString(answer.answerRes)
        button.setOnClickListener {
            when (answer.isCorrect) {
                true -> onCorrectAnswerClicked()
                false -> onIncorrectAnswerClicked()
            }
        }
    }

    private fun onCorrectAnswerClicked() {
        showToast(R.string.correct_answer)
        refresh()
    }

    private fun onIncorrectAnswerClicked() {
        showToast(R.string.incorrect_answer)
        refresh()
    }

    private fun getQuestionProps(): QuestionProps {
        val questionNumber = Random.nextInt(1, 5)

        val correctAnswer = QuestionProps.Answer(
            answerRes = getStringByName(ANSWER_CORRECT_FORMAT.format(questionNumber)),
            isCorrect = true
        )

        val incorrectAnswer = QuestionProps.Answer(
            answerRes = getStringByName(ANSWER_INCORRECT_FORMAT.format(questionNumber, 1)),
            isCorrect = false
        )

        val questionRes = getStringByName(QUESTION_FORMAT.format(questionNumber))
        val answers = listOf(correctAnswer, incorrectAnswer).shuffled()

        return QuestionProps(questionRes, answers)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private data class QuestionProps(@StringRes val questionRes: Int, val answers: List<Answer>) {
        data class Answer(@StringRes val answerRes: Int, val isCorrect: Boolean)
    }

}
