package com.example.data.repository

import android.util.Log
import com.example.data.local.dao.QuestionDao
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.preferences.UserPreferences
import com.example.data.mapper.toDomain
import com.example.data.remote.QuizApi
import com.example.data.remote.dto.toEntity
import com.example.domain.model.Question
import com.example.domain.repository.QuizRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val userPreferences: UserPreferences,
    private val quizApi: QuizApi,
) : QuizRepository {

    override fun getQuestions(): Flow<List<Question>> = flow {
        // 1. Najpierw wyemituj to, co już mamy w bazie, żeby użytkownik nie widział pustego ekranu
        val localData = questionDao.getQuestions().first().map { it.toDomain() }
        if (localData.isNotEmpty()) {
            emit(localData)
        }

        // 2. Próbuj pobrać świeże dane z sieci
        try {
            val remoteQuestions = quizApi.getQuestions()
            if (remoteQuestions.isNotEmpty()) {
                // Mapowanie i zapis do bazy (OnConflictStrategy.REPLACE obsłuży unikalność)
                questionDao.insertQuestions(remoteQuestions.map { it.toEntity() })
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Network fetch failed: \${e.message}")
            // Jeśli baza jest kompletnie pusta i sieć zawiodła, dodaj pytania startowe
            if (questionDao.getQuestionCount() == 0) {
                questionDao.insertQuestions(getInitialQuestions())
            }
        }

        // 3. Emituj ostateczny stan bazy (lokalne + pobrane)
        emitAll(
            questionDao.getQuestions().map { entities ->
                entities.map { it.toDomain() }
            }
        )
    }.distinctUntilChanged()

    override fun getHighScore(): Flow<Int> = userPreferences.highScore

    override suspend fun saveHighScore(score: Int) {
        userPreferences.saveHighScore(score)
    }

    private fun getInitialQuestions(): List<QuestionEntity> {
        return listOf(
            QuestionEntity(
                text = "Who is the author of The Lord of the Rings?",
                options = "J.K. Rowling|J.R.R. Tolkien|C.S. Lewis|George R.R. Martin",
                correctAnswerIndex = 1,
                category = "General"
            )
        )
    }
}
