package com.example.domain.usecase

import com.example.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHighScoreUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(): Flow<Int> = repository.getHighScore()
}
