package com.dastan.weatherfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dastan.weatherfinal.presentation.favorites.FavoritesScreen
import com.dastan.weatherfinal.presentation.weather.WeatherScreen
import com.dastan.weatherfinal.ui.theme.WeatherFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherFinalTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, null) },
                                label = { Text("Weather") },
                                selected = currentRoute == "weather",
                                onClick = {
                                    navController.navigate("weather") {
                                        popUpTo("weather") { inclusive = true }
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Favorite, null) },
                                label = { Text("Favorites") },
                                selected = currentRoute == "favorites",
                                onClick = {
                                    navController.navigate("favorites") {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "weather",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("weather") { WeatherScreen() }
                        composable("favorites") { FavoritesScreen() }
                    }
                }
            }
        }
    }
}