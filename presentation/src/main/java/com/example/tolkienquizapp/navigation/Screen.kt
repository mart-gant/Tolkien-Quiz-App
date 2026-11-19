package com.example.tolkienquizapp.navigation

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Quiz : Screen("quiz?isSuddenDeath={isSuddenDeath}&timerSeconds={timerSeconds}") {
        fun createRoute(isSuddenDeath: Boolean, timerSeconds: Int = 15) = 
            "quiz?isSuddenDeath=$isSuddenDeath&timerSeconds=$timerSeconds"
    }
}
