package com.martgant.tolkienquizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.martgant.tolkienquizapp.databinding.FragmentQuestionBinding
import kotlin.random.Random

class QuestionFragment : Fragment() {

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
        val (questionRes, answers) = getQuestionProps()
        binding.questionDescription.text = context?.getString(questionRes)
        bindButton(binding.answer1, answers[0])
        bindButton(binding.answer2, answers[1])
    }

    // Since adding text and on click listener to button would have to happen for each button - let's extract it to separate method.
    // It's not yet in it's final form
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
        Toast.makeText(requireContext(), R.string.correct_answer, Toast.LENGTH_LONG).show()
    }

    private fun onIncorrectAnswerClicked() {
        Toast.makeText(requireContext(), R.string.incorrect_answer, Toast.LENGTH_LONG).show()
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

    @StringRes
    private fun getStringByName(stringName: String): Int {
        return resources.getIdentifier(stringName, "string", activity?.packageName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private data class QuestionProps(@StringRes val questionRes: Int, val answers: List<Answer>) {
        data class Answer(@StringRes val answerRes: Int, val isCorrect: Boolean)
    }

}
