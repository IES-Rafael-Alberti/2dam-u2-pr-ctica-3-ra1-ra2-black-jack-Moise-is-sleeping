package com.calculator.blackjackmoise.model

sealed class Routes(val route : String) {
    object Player1 : Routes("Player 1")
    object MultiplayerScreen : Routes("MultiplayerScreen")
    object MainMenu : Routes("MainMenu")


}
