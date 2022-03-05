package com.martgant.tolkienquizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.martgant.tolkienquizapp.databinding.FragmentQuestionBinding

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
        val props = QuestionProps(
            questionRes = R.string.question_1,
            answer1Res = R.string.question_1_answer_correct,
            answer2Res = R.string.question_1_answer_incorrect_1
        )
        binding.questionDescription.text = context?.getString(props.questionRes)
        binding.answer1.text = context?.getString(props.answer1Res)
        binding.answer2.text = context?.getString(props.answer2Res)
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
