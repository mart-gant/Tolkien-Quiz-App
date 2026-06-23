package com.example.tolkienquizapp.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QuizScreen(
    onNavigateBack: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isFinished) {
            QuizResult(
                score = uiState.score,
                total = uiState.questions.size,
                onBackToMenu = onNavigateBack
            )
        } else if (uiState.questions.isNotEmpty()) {
            val currentQuestion = uiState.questions[uiState.currentQuestionIndex]
            QuizContent(
                questionText = currentQuestion.text,
                options = currentQuestion.options,
                selectedOptionIndex = uiState.selectedOptionIndex,
                onOptionSelected = viewModel::onOptionSelected,
                onNextClicked = viewModel::onNextClicked,
                questionNumber = uiState.currentQuestionIndex + 1,
                totalQuestions = uiState.questions.size
            )
        }
    }
}

@Composable
fun QuizContent(
    questionText: String,
    options: List<String>,
    selectedOptionIndex: Int?,
    onOptionSelected: (Int) -> Unit,
    onNextClicked: () -> Unit,
    questionNumber: Int,
    totalQuestions: Int
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight
        
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Question $questionNumber / $totalQuestions",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = questionText,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    OptionsList(options, selectedOptionIndex, onOptionSelected)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onNextClicked,
                        enabled = selectedOptionIndex != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Next")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Question $questionNumber / $totalQuestions",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = questionText,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                OptionsList(options, selectedOptionIndex, onOptionSelected)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onNextClicked,
                    enabled = selectedOptionIndex != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun OptionsList(
    options: List<String>,
    selectedOptionIndex: Int?,
    onOptionSelected: (Int) -> Unit
) {
    options.forEachIndexed { index, option ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (index == selectedOptionIndex),
                    onClick = { onOptionSelected(index) }
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (index == selectedOptionIndex),
                onClick = { onOptionSelected(index) }
            )
            Text(
                text = option,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun QuizResult(score: Int, total: Int, onBackToMenu: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Finished!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your score: $score / $total",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBackToMenu) {
            Text("Back to Menu")
        }
    }
}