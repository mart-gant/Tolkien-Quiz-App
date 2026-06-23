package com.example.data.mapper

import com.example.data.local.entity.QuestionEntity
import com.example.domain.model.Question

fun QuestionEntity.toDomain(): Question {
    return Question(
        id = id,
        text = text,
        options = options.split("|"),
        correctAnswerIndex = correctAnswerIndex
    )
}

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        text = text,
        options = options.joinToString("|"),
        correctAnswerIndex = correctAnswerIndex
    )
}