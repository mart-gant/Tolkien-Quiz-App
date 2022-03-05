package com.martgant.tolkienquizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.questionDescription.text = context?.getString(R.string.question_1)
        binding.answer1.text = context?.getString(R.string.question_1_answer_correct)
        binding.answer2.text = context?.getString(R.string.question_1_answer_incorrect_1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
