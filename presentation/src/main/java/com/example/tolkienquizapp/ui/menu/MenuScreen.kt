package com.example.tolkienquizapp.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tolkienquizapp.R

@Composable
fun MenuScreen(
    onStartQuiz: (isSuddenDeath: Boolean, timerSeconds: Int) -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val highScore by viewModel.highScore.collectAsState()
    var isSuddenDeath by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableIntStateOf(1) } // 0: Shire (20s), 1: Gondor (15s), 2: Mordor (10s)
    var showAboutDialog by remember { mutableStateOf(false) }

    val timerSeconds = when (selectedDifficulty) {
        0 -> 20
        2 -> 10
        else -> 15
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
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.tolkien_quiz_banner),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Image(
            painter = painterResource(id = R.drawable.tolkien_quiz_logo),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.test_knowledge),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        if (highScore > 0) {
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.high_score, highScore),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Poziom trudności / Poziom krainy:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            FilterChip(
                selected = selectedDifficulty == 0,
                onClick = { selectedDifficulty = 0 },
                label = { Text("🌱 Shire (20s)") }
            )
            FilterChip(
                selected = selectedDifficulty == 1,
                onClick = { selectedDifficulty = 1 },
                label = { Text("🏰 Gondor (15s)") }
            )
            FilterChip(
                selected = selectedDifficulty == 2,
                onClick = { selectedDifficulty = 2 },
                label = { Text("🌋 Mordor (10s)") }
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Checkbox(
                checked = isSuddenDeath,
                onCheckedChange = { isSuddenDeath = it }
            )
            Text(
                text = stringResource(R.string.sudden_death_mode),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = { onStartQuiz(isSuddenDeath, timerSeconds) },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(stringResource(R.string.start_quiz))
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = { showAboutDialog = true }) {
            Text(stringResource(R.string.about))
        }
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(stringResource(R.string.about)) },
            text = { Text(stringResource(R.string.legal_disclaimer)) },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
