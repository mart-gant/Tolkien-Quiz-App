package com.example.tolkienquizapp.ui.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Question
import com.example.domain.usecase.GetQuestionsUseCase
import com.example.domain.usecase.SaveHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val selectedOptionIndex: Int? = null,
    val timeLeft: Int = 15,
    val isTimeUp: Boolean = false,
    val isSuddenDeath: Boolean = false,
    val wasSuddenDeathFailed: Boolean = false,
    val isLoading: Boolean = true,
    val loadingQuote: String = ""
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveHighScoreUseCase: SaveHighScoreUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isSuddenDeathMode: Boolean = savedStateHandle["isSuddenDeath"] ?: false

    private val _uiState = MutableStateFlow(QuizUiState(
        isSuddenDeath = isSuddenDeathMode,
        loadingQuote = getRandomQuote()
    ))
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Sztuczne opóźnienie (np. 2s), żeby użytkownik mógł nacieszyć się cytatem
            delay(2000L) 
            
            getQuestionsUseCase().collect { questions ->
                if (questions.isNotEmpty()) {
                    _uiState.update { it.copy(
                        questions = questions.shuffled(),
                        isLoading = false
                    ) }
                    startTimer()
                }
            }
        }
    }

    private fun getRandomQuote(): String {
        val quotes = listOf(
            "Not all those who wander are lost.",
            "Even the smallest person can change the course of the future.",
            "All we have to decide is what to do with the time that is given us.",
            "The world is indeed full of peril, and in it there are many dark places.",
            "Faithless is he that says farewell when the road darkens.",
            "Home is behind, the world ahead."
        )
        return quotes.random()
    }

    private fun startTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(timeLeft = 15, isTimeUp = false) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0) {
                delay(1000L)
                _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
            }
            onTimeUp()
        }
    }

    private fun onTimeUp() {
        _uiState.update { it.copy(isTimeUp = true) }
        if (isSuddenDeathMode) {
            finishQuiz(failedSuddenDeath = true)
        } else {
            viewModelScope.launch {
                delay(1500L)
                onNextClicked()
            }
        }
    }

    fun onOptionSelected(index: Int) {
        if (_uiState.value.isTimeUp || _uiState.value.isFinished || _uiState.value.isLoading) return
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun onNextClicked() {
        timerJob?.cancel()
        val state = _uiState.value
        if (state.isFinished || state.isLoading) return

        val isCorrect = state.selectedOptionIndex == state.questions[state.currentQuestionIndex].correctAnswerIndex
        
        if (isSuddenDeathMode && !isCorrect) {
            finishQuiz(failedSuddenDeath = true)
            return
        }

        val nextIndex = state.currentQuestionIndex + 1
        val isFinished = nextIndex >= state.questions.size

        if (isFinished) {
            val finalScore = if (isCorrect) state.score + 1 else state.score
            finishQuiz(finalScore = finalScore)
        } else {
            _uiState.update { 
                it.copy(
                    score = if (isCorrect) it.score + 1 else it.score,
                    currentQuestionIndex = nextIndex,
                    selectedOptionIndex = null,
                    timeLeft = 15,
                    isTimeUp = false
                )
            }
            startTimer()
        }
    }

    private fun finishQuiz(finalScore: Int? = null, failedSuddenDeath: Boolean = false) {
        val scoreToSave = finalScore ?: _uiState.value.score
        viewModelScope.launch {
            saveHighScoreUseCase(scoreToSave)
        }
        _uiState.update { 
            it.copy(
                score = scoreToSave,
                isFinished = true,
                wasSuddenDeathFailed = failedSuddenDeath
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
