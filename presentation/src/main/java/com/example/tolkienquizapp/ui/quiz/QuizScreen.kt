package com.example.tolkienquizapp.ui.quiz

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tolkienquizapp.R
import com.example.tolkienquizapp.ui.theme.TolkienQuizTheme

@Composable
fun QuizScreen(
    onNavigateBack: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            LoadingScreen(quote = uiState.loadingQuote)
        } else {
            AnimatedContent(
                targetState = uiState.isFinished,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }, label = "quiz_content"
            ) { finished ->
                if (finished) {
                    QuizResult(
                        score = uiState.score,
                        total = uiState.questions.size,
                        wasSuddenDeathFailed = uiState.wasSuddenDeathFailed,
                        onBackToMenu = onNavigateBack
                    )
                } else if (uiState.questions.isNotEmpty()) {
                    val currentQuestion = uiState.questions[uiState.currentQuestionIndex]
                    QuizContent(
                        questionText = currentQuestion.text,
                        category = currentQuestion.category,
                        options = currentQuestion.options,
                        selectedOptionIndex = uiState.selectedOptionIndex,
                        correctAnswerIndex = currentQuestion.correctAnswerIndex,
                        showAnswerResult = uiState.showAnswerResult,
                        loreNote = currentQuestion.loreNote,
                        onOptionSelected = viewModel::onOptionSelected,
                        onNextClicked = viewModel::onNextClicked,
                        questionNumber = uiState.currentQuestionIndex + 1,
                        totalQuestions = uiState.questions.size,
                        timeLeft = uiState.timeLeft,
                        isTimeUp = uiState.isTimeUp,
                        isSuddenDeath = uiState.isSuddenDeath
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(quote: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "\"$quote\"",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "— J.R.R. Tolkien",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun QuizContent(
    questionText: String,
    category: String,
    options: List<String>,
    selectedOptionIndex: Int?,
    correctAnswerIndex: Int,
    showAnswerResult: Boolean,
    loreNote: String = "",
    onOptionSelected: (Int) -> Unit,
    onNextClicked: () -> Unit,
    questionNumber: Int,
    totalQuestions: Int,
    timeLeft: Int,
    isTimeUp: Boolean,
    isSuddenDeath: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.question_progress, questionNumber, totalQuestions),
                style = MaterialTheme.typography.labelLarge
            )
            
            TimerDisplay(timeLeft = timeLeft)
        }
        
        LinearProgressIndicator(
            progress = { questionNumber.toFloat() / totalQuestions },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            color = if (isSuddenDeath) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        if (isSuddenDeath) {
            Text(
                text = stringResource(R.string.sudden_death_mode),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = category,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = questionText,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Column(modifier = Modifier.weight(2f)) {
            OptionsList(
                options = options,
                selectedOptionIndex = selectedOptionIndex,
                correctAnswerIndex = correctAnswerIndex,
                showAnswerResult = showAnswerResult,
                onOptionSelected = onOptionSelected,
                enabled = !isTimeUp && !showAnswerResult
            )

            AnimatedVisibility(
                visible = showAnswerResult && loreNote.isNotEmpty(),
                enter = fadeIn() + expandVertically()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 Czy wiesz, że...?",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = loreNote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        if (isTimeUp) {
            Text(
                text = stringResource(R.string.time_up),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNextClicked,
            enabled = (selectedOptionIndex != null) || isTimeUp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                if (questionNumber == totalQuestions) stringResource(R.string.finish) 
                else stringResource(R.string.next)
            )
        }
    }
}

@Composable
fun TimerDisplay(timeLeft: Int) {
    val color = if (timeLeft <= 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.extraLarge,
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text = "00:${timeLeft.toString().padStart(2, '0')}",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun OptionsList(
    options: List<String>,
    selectedOptionIndex: Int?,
    correctAnswerIndex: Int,
    showAnswerResult: Boolean,
    onOptionSelected: (Int) -> Unit,
    enabled: Boolean
) {
    options.forEachIndexed { index, option ->
        val isSelected = index == selectedOptionIndex
        val isCorrect = index == correctAnswerIndex

        val containerColor = when {
            showAnswerResult && isCorrect -> Color(0xFF2E7D32).copy(alpha = 0.15f)
            showAnswerResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            else -> Color.Transparent
        }

        val borderColor = when {
            showAnswerResult && isCorrect -> Color(0xFF2E7D32)
            showAnswerResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outline
        }

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .selectable(
                    selected = isSelected,
                    enabled = enabled,
                    onClick = { onOptionSelected(index) }
                ),
            colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
            border = androidx.compose.foundation.BorderStroke(
                width = if (isSelected || (showAnswerResult && isCorrect)) 2.dp else 1.dp,
                color = borderColor
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = null,
                    enabled = enabled,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = if (showAnswerResult && !isCorrect) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = option,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (showAnswerResult && isCorrect) FontWeight.Bold else FontWeight.Normal,
                    color = if (showAnswerResult && isCorrect) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun QuizResult(score: Int, total: Int, wasSuddenDeathFailed: Boolean, onBackToMenu: () -> Unit) {
    val percentage = if (total > 0) (score.toFloat() / total * 100).toInt() else 0
    val (rankTitle, rankEmoji) = when {
        percentage == 100 -> "Prawdziwy Powiernik Pierścienia" to "💍"
        percentage >= 75 -> "Uczony z Gondoru" to "📜"
        percentage >= 50 -> "Strażnik z Północy" to "⚔️"
        percentage >= 25 -> "Podróżnik z Bree" to "🍺"
        else -> "Hobbit nieopuszczający Shire" to "🪵"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.quiz_finished),
            style = MaterialTheme.typography.displaySmall,
            color = if (wasSuddenDeathFailed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = rankEmoji, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = rankTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        if (wasSuddenDeathFailed) {
            Text(
                text = stringResource(R.string.sudden_death_over),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        CircularProgressIndicator(
            progress = { score.toFloat() / total },
            modifier = Modifier.size(110.dp),
            strokeWidth = 12.dp,
            color = if (wasSuddenDeathFailed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = stringResource(R.string.your_score, score, total),
            style = MaterialTheme.typography.headlineSmall
        )
        
        val feedback = when {
            percentage == 100 -> stringResource(R.string.feedback_perfect)
            percentage >= 70 -> stringResource(R.string.feedback_good)
            percentage >= 40 -> stringResource(R.string.feedback_average)
            else -> stringResource(R.string.feedback_poor)
        }
        
        Text(
            text = feedback,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp),
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(36.dp))
        Button(
            onClick = onBackToMenu,
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = if (wasSuddenDeathFailed) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.buttonColors()
        ) {
            Text(stringResource(R.string.back_to_menu))
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuizContentPreview() {
    TolkienQuizTheme {
        QuizContent(
            questionText = "Who is the author of The Lord of the Rings?",
            category = "General",
            options = listOf("J.K. Rowling", "J.R.R. Tolkien", "C.S. Lewis", "George R.R. Martin"),
            selectedOptionIndex = 1,
            correctAnswerIndex = 1,
            showAnswerResult = true,
            onOptionSelected = {},
            onNextClicked = {},
            questionNumber = 1,
            totalQuestions = 10,
            timeLeft = 12,
            isTimeUp = false,
            isSuddenDeath = false
        )
    }
}
