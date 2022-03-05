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
        // This is called destructuring
        // https://kotlinlang.org/docs/destructuring-declarations.html
        val (questionRes, answer1Res, answer2Res) = getQuestionProps()
        binding.questionDescription.text = context?.getString(questionRes)
        binding.answer1.text = context?.getString(answer1Res)
        binding.answer2.text = context?.getString(answer2Res)
    }

    private fun getQuestionProps(): QuestionProps {
        val questionNumber = Random.nextInt(1, 5)
        // This is a string that will be used to get string from resources. %d is "placeholder"
        // which will be replaced with number later
        val questionFormat = "question_%d"
        val answerCorrectFormat = "question_%d_answer_correct"
        val answerIncorrectFormat = "question_%d_answer_incorrect_%d"

        return QuestionProps(
            // to get string based on string you need to use `resources.getIdentifier`
            // the parameters are: 1) name of the string you want, 2) type of resource (in this case "string")
            // 3) package name in which the string is placed - for now it's always the same
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
