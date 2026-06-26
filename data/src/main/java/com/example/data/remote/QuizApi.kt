package com.example.data.remote

import com.example.data.remote.dto.QuestionDto
import retrofit2.http.GET

interface QuizApi {
    // Pobiera plik bezpośrednio z surowych zasobów GitHub
    @GET("questions.json")
    suspend fun getQuestions(): List<QuestionDto>
    
    companion object {
        // ZMIEŃ TO: Wstaw tutaj link do swojego profilu/repozytorium
        // Pamiętaj o końcowym ukośniku!
        const val BASE_URL = "https://raw.githubusercontent.com/marcingantkowski/Tolkien-Quiz-App/main/"
    }
}
