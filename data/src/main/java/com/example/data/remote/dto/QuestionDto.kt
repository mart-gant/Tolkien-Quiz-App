package com.example.data.remote.dto

import com.squareup.moshi.Json
import com.example.data.local.entity.QuestionEntity

data class QuestionDto(
    @Json(name = "text") val text: String,
    @Json(name = "options") val options: List<String>,
    @Json(name = "correctAnswerIndex") val correctAnswerIndex: Int,
    @Json(name = "category") val category: String
)

fun QuestionDto.toEntity(): QuestionEntity {
    return QuestionEntity(
        text = text,
        options = options.joinToString("|"),
        correctAnswerIndex = correctAnswerIndex,
        category = category
    )
}
