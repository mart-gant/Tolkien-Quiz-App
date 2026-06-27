package com.example.tolkienquizapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.tolkienquizapp.ui.quiz.QuizContent
import com.example.tolkienquizapp.ui.theme.TolkienQuizTheme
import org.junit.Rule
import org.junit.Test

class QuizUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun quizContent_displaysQuestionAndOptions() {
        val question = "Who is the author of The Lord of the Rings?"
        val options = listOf("J.K. Rowling", "J.R.R. Tolkien", "C.S. Lewis", "George R.R. Martin")

        composeTestRule.setContent {
            TolkienQuizTheme {
                QuizContent(
                    questionText = question,
                    category = "General",
                    options = options,
                    selectedOptionIndex = null,
                    onOptionSelected = {},
                    onNextClicked = {},
                    questionNumber = 1,
                    totalQuestions = 10,
                    timeLeft = 15,
                    isTimeUp = false,
                    isSuddenDeath = false,
                )
            }
        }

        // Check if question is displayed
        composeTestRule.onNodeWithText(question).assertIsDisplayed()

        // Check if all options are displayed
        options.forEach { option ->
            composeTestRule.onNodeWithText(option).assertIsDisplayed()
        }
    }

    @Test
    fun selectingOption_enablesNextButton() {
        composeTestRule.setContent {
            var stateIndex by remember { mutableStateOf<Int?>(null) }
            TolkienQuizTheme {
                QuizContent(
                    questionText = "Question",
                    category = "Cat",
                    options = listOf("Opt 1", "Opt 2"),
                    selectedOptionIndex = stateIndex,
                    onOptionSelected = { stateIndex = it },
                    onNextClicked = {},
                    questionNumber = 1,
                    totalQuestions = 10,
                    timeLeft = 15,
                    isTimeUp = false,
                    isSuddenDeath = false,
                )
            }
        }

        // Next button should be disabled initially
        // Using substring match for "Next" since it might be in different languages, 
        // but for test we assume English or specific resource
        composeTestRule.onAllNodesWithText("Next", substring = true).onFirst().assertIsNotEnabled()

        // Click on first option
        composeTestRule.onNodeWithText("Opt 1").performClick()

        // Next button should now be enabled
        composeTestRule.onAllNodesWithText("Next", substring = true).onFirst().assertIsEnabled()
    }

    @Test
    fun timeUp_displaysTimeUpMessage() {
        composeTestRule.setContent {
            TolkienQuizTheme {
                QuizContent(
                    questionText = "Question",
                    category = "Cat",
                    options = listOf("Opt 1"),
                    selectedOptionIndex = null,
                    onOptionSelected = {},
                    onNextClicked = {},
                    questionNumber = 1,
                    totalQuestions = 10,
                    timeLeft = 0,
                    isTimeUp = true,
                    isSuddenDeath = false
                )
            }
        }

        // Message should be visible
        composeTestRule.onNodeWithText("Time is up!", substring = true).assertIsDisplayed()
    }
}
