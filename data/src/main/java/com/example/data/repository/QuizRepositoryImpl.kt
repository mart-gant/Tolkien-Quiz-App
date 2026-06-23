package com.example.data.repository

import com.example.data.local.dao.QuestionDao
import com.example.data.local.entity.QuestionEntity
import com.example.data.mapper.toDomain
import com.example.domain.model.Question
import com.example.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : QuizRepository {

    override fun getQuestions(): Flow<List<Question>> = flow {
        if (questionDao.getQuestionCount() == 0) {
            questionDao.insertQuestions(getInitialQuestions())
        }
        emitAll(questionDao.getQuestions().map { entities ->
            entities.map { it.toDomain() }
        })
    }

    private fun getInitialQuestions(): List<QuestionEntity> {
        return listOf(
            QuestionEntity(
                text = "Who is the author of The Lord of the Rings?",
                options = "J.K. Rowling|J.R.R. Tolkien|C.S. Lewis|George R.R. Martin",
                correctAnswerIndex = 1
            ),
            QuestionEntity(
                text = "What is the name of the One Ring's creator?",
                options = "Sauron|Saruman|Gandalf|Morgoth",
                correctAnswerIndex = 0
            ),
            QuestionEntity(
                text = "Which creature is Gollum?",
                options = "Elf|Hobbit-like creature|Orc|Dwarf",
                correctAnswerIndex = 1
            ),
            QuestionEntity(
                text = "What is the capital of Gondor?",
                options = "Edoras|Osgiliath|Minas Tirith|Rivendell",
                correctAnswerIndex = 2
            ),
            QuestionEntity(
                text = "Who destroyed the One Ring?",
                options = "Frodo Baggins|Samwise Gamgee|Gollum|Aragorn",
                correctAnswerIndex = 2
            ),
            QuestionEntity(
                text = "How many Rings of Power were given to the Dwarf-lords?",
                options = "Three|Seven|Nine|One",
                correctAnswerIndex = 1
            ),
            QuestionEntity(
                text = "What is the name of Gandalf's horse?",
                options = "Bill|Arod|Shadowfax|Hasufel",
                correctAnswerIndex = 2
            ),
            QuestionEntity(
                text = "Which forest is the home of the Ents?",
                options = "Mirkwood|Fangorn Forest|Lothlórien|Old Forest",
                correctAnswerIndex = 1
            ),
            QuestionEntity(
                text = "Who is the rightful King of Gondor?",
                options = "Boromir|Aragorn|Denethor|Faramir",
                correctAnswerIndex = 1
            ),
            QuestionEntity(
                text = "What was the name of the sword Frodo carried?",
                options = "Glamdring|Andúril|Sting|Orcrist",
                correctAnswerIndex = 2
            )
        )
    }
}