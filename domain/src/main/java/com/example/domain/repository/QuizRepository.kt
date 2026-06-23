package com.example.domain.repository

import com.example.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getQuestions(): Flow<List<Question>>
}