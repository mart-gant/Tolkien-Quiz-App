package com.example.tolkienquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tolkienquizapp.ui.theme.TolkienQuizTheme
import com.example.tolkienquizapp.ui.menu.MenuScreen
import com.example.tolkienquizapp.ui.quiz.QuizScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TolkienQuizTheme {
                TolkienQuizApp()
            }
        }
    }
}

@Composable
fun TolkienQuizApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "menu"
    ) {
        composable("menu") {
            MenuScreen(
                onStartQuiz = { navController.navigate("quiz") }
            )
        }
        composable("quiz") {
            QuizScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}