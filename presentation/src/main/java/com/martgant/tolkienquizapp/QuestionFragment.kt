package com.martgant.tolkienquizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // Since now you receive list of answers to access each one you need to do answers[X] where X is an index. Of course starting from 0.
        binding.answer1.text = context?.getString(answers[0].answerRes)
        binding.answer2.text = context?.getString(answers[1].answerRes)
    }

    private fun getQuestionProps(): QuestionProps {
        val questionNumber = Random.nextInt(1, 5)

        val correctAnswer = QuestionProps.Answer(
            answerRes = resources.getIdentifier(ANSWER_CORRECT_FORMAT.format(questionNumber), "string", activity?.packageName),
            isCorrect = true
        )

        val incorrectAnswer = QuestionProps.Answer(
            answerRes = resources.getIdentifier(ANSWER_INCORRECT_FORMAT.format(questionNumber, 1), "string", activity?.packageName),
            isCorrect = false
        )

        val questionRes = resources.getIdentifier(QUESTION_FORMAT.format(questionNumber), "string", activity?.packageName)
        val answers = listOf(correctAnswer, incorrectAnswer).shuffled()

        return QuestionProps(questionRes, answers)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // `answers` is now a list - so each question can have potentially a lot of possible answers. Of course now it's always 2 but it will change later
    private data class QuestionProps(@StringRes val questionRes: Int, val answers: List<Answer>) {
        // This is the way to represent the answer - you need to know the text to display and if the answer is correct or not
        data class Answer(@StringRes val answerRes: Int, val isCorrect: Boolean)
    }

}
