package com.calculator.blackjackmoise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import Data.Routes
import UI.MainMenu
import UI.MultiplayerScreen
import UI.SinglePlayerScreen
import UI.ViewModel


class MainActivity : ComponentActivity() {
    private val viewModel : ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.MainMenu.route){
                    composable(Routes.MainMenu.route){
                        MainMenu(navController, viewModel)
                    }
                    composable(Routes.MultiplayerScreen.route){
                        MultiplayerScreen(navController,viewModel)
                    }
                    composable(Routes.SinglePlayerScreen.route){
                        SinglePlayerScreen(navController,viewModel)
                    }

                }
            }
        }

    }
}
