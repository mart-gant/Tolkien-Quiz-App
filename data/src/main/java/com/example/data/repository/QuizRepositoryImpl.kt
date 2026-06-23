package com.example.data.repository

import com.example.domain.model.Question
import com.example.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor() : QuizRepository {
    override fun getQuestions(): Flow<List<Question>> = flow {
        emit(
            listOf(
                Question(
                    id = 1,
                    text = "Who is the author of The Lord of the Rings?",
                    options = listOf("J.K. Rowling", "J.R.R. Tolkien", "C.S. Lewis", "George R.R. Martin"),
                    correctAnswerIndex = 1
                ),
                Question(
                    id = 2,
                    text = "What is the name of the One Ring's creator?",
                    options = listOf("Sauron", "Saruman", "Gandalf", "Morgoth"),
                    correctAnswerIndex = 0
                ),
                Question(
                    id = 3,
                    text = "Which creature is Gollum?",
                    options = listOf("Elf", "Hobbit-like creature", "Orc", "Dwarf"),
                    correctAnswerIndex = 1
                ),
                Question(
                    id = 4,
                    text = "What is the capital of Gondor?",
                    options = listOf("Edoras", "Osgiliath", "Minas Tirith", "Rivendell"),
                    correctAnswerIndex = 2
                ),
                Question(
                    id = 5,
                    text = "Who destroyed the One Ring?",
                    options = listOf("Frodo Baggins", "Samwise Gamgee", "Gollum", "Aragorn"),
                    correctAnswerIndex = 2
                )
            )
        )
    }
}