package com.example.tolkienquizapp.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Question
import com.example.domain.usecase.GetQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val isFinished: Boolean = false,
    val selectedOptionIndex: Int? = null
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            getQuestionsUseCase().collect { questions ->
                _uiState.update { it.copy(questions = questions) }
            }
        }
    }

    fun onOptionSelected(index: Int) {
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun onNextClicked() {
        val state = _uiState.value
        val isCorrect = state.selectedOptionIndex == state.questions[state.currentQuestionIndex].correctAnswerIndex
        
        val nextIndex = state.currentQuestionIndex + 1
        val isFinished = nextIndex >= state.questions.size

        _uiState.update { 
            it.copy(
                score = if (isCorrect) it.score + 1 else it.score,
                currentQuestionIndex = if (isFinished) it.currentQuestionIndex else nextIndex,
                isFinished = isFinished,
                selectedOptionIndex = null
            )
        }
    }
}