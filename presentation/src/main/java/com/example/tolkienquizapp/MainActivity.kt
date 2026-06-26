package com.example.tolkienquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tolkienquizapp.ui.theme.TolkienQuizTheme
import com.example.tolkienquizapp.ui.menu.MenuScreen
import com.example.tolkienquizapp.ui.quiz.QuizScreen
import com.example.tolkienquizapp.navigation.Screen
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
        startDestination = Screen.Menu.route
    ) {
        composable(Screen.Menu.route) {
            MenuScreen(
                onStartQuiz = { isSuddenDeath -> 
                    navController.navigate(Screen.Quiz.createRoute(isSuddenDeath)) 
                }
            )
        }
        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("isSuddenDeath") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            QuizScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
