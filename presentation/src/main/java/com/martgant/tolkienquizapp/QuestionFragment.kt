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
        val (questionRes, answer1Res, answer2Res) = getQuestionProps()
        binding.questionDescription.text = context?.getString(questionRes)
        binding.answer1.text = context?.getString(answer1Res)
        binding.answer2.text = context?.getString(answer2Res)
    }

    private fun getQuestionProps(): QuestionProps {
        val questionNumber = Random.nextInt(1, 5)
        val questionFormat = "question_%d"
        val answerCorrectFormat = "question_%d_answer_correct"
        val answerIncorrectFormat = "question_%d_answer_incorrect_%d"

        return QuestionProps(
            questionRes = resources.getIdentifier(
                questionFormat.format(questionNumber),
                "string",
                activity?.packageName
            ),
            answer1Res = resources.getIdentifier(
                answerCorrectFormat.format(questionNumber),
                "string",
                activity?.packageName
            ),
            answer2Res = resources.getIdentifier(
                answerIncorrectFormat.format(questionNumber, 1),
                "string",
                activity?.packageName
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private data class QuestionProps(
        @StringRes val questionRes: Int,
        @StringRes val answer1Res: Int,
        @StringRes val answer2Res: Int
    )
}
