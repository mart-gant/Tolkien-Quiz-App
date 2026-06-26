package com.example.tolkienquizapp.navigation

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Quiz : Screen("quiz?isSuddenDeath={isSuddenDeath}") {
        fun createRoute(isSuddenDeath: Boolean) = "quiz?isSuddenDeath=$isSuddenDeath"
    }
}
