package com.example.tolkienquizapp.ui.quiz

import androidx.lifecycle.SavedStateHandle
import com.example.domain.model.Question
import com.example.domain.usecase.GetQuestionsUseCase
import com.example.domain.usecase.SaveHighScoreUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val getQuestionsUseCase: GetQuestionsUseCase = mockk()
    private val saveHighScoreUseCase: SaveHighScoreUseCase = mockk(relaxed = true)
    private lateinit var viewModel: QuizViewModel
    
    private val testDispatcher = StandardTestDispatcher()

    private fun createViewModel(isSuddenDeath: Boolean = false) {
        val savedStateHandle = SavedStateHandle(mapOf("isSuddenDeath" to isSuddenDeath))
        viewModel = QuizViewModel(getQuestionsUseCase, saveHighScoreUseCase, savedStateHandle)
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        val questions = listOf(
            Question(1, "Q1", listOf("A", "B"), 0, "Cat1"),
            Question(2, "Q2", listOf("C", "D"), 1, "Cat2"),
        )
        coEvery { getQuestionsUseCase() } returns flowOf(questions)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(2, state.questions.size)
        assertEquals(0, state.currentQuestionIndex)
        assertEquals(0, state.score)
    }

    @Test
    fun `answering correctly increases score`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val firstQuestion = viewModel.uiState.value.questions[0]
        viewModel.onOptionSelected(firstQuestion.correctAnswerIndex)
        viewModel.onNextClicked()
        
        assertEquals(1, viewModel.uiState.value.score)
        assertEquals(1, viewModel.uiState.value.currentQuestionIndex)
    }

    @Test
    fun `sudden death mode ends game on wrong answer`() = runTest {
        createViewModel(isSuddenDeath = true)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val firstQuestion = viewModel.uiState.value.questions[0]
        val wrongIndex = if (firstQuestion.correctAnswerIndex == 0) 1 else 0
        
        viewModel.onOptionSelected(wrongIndex)
        viewModel.onNextClicked()
        
        val state = viewModel.uiState.value
        assertEquals(true, state.isFinished)
        assertEquals(true, state.wasSuddenDeathFailed)
        assertEquals(0, state.score)
    }

    @Test
    fun `finishing quiz saves high score`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Answer Q1
        val q1 = viewModel.uiState.value.questions[0]
        viewModel.onOptionSelected(q1.correctAnswerIndex)
        viewModel.onNextClicked()
        
        // Answer Q2
        val q2 = viewModel.uiState.value.questions[1]
        viewModel.onOptionSelected(q2.correctAnswerIndex)
        viewModel.onNextClicked()
        
        assertEquals(2, viewModel.uiState.value.score)
        assertEquals(true, viewModel.uiState.value.isFinished)
        
        coVerify { saveHighScoreUseCase(2) }
    }
}
