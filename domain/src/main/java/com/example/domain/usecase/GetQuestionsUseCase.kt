package com.example.domain.usecase

import com.example.domain.model.Question
import com.example.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(): Flow<List<Question>> = repository.getQuestions()
}