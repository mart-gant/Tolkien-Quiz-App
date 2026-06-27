package com.example.domain.usecase

import com.example.domain.repository.QuizRepository
import javax.inject.Inject

class SaveHighScoreUseCase @Inject constructor(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke(score: Int) = repository.saveHighScore(score)
}
