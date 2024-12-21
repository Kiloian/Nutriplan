package com.nutriplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nutriplan.app.navigation.BottomNavigationBar
import com.nutriplan.app.navigation.NavRoutes
import com.nutriplan.app.ui.mealplan.MealPlanningScreen
import com.nutriplan.app.ui.recipes.RecipesScreen
import com.nutriplan.app.ui.shopping.ShoppingListScreen
import com.nutriplan.app.ui.theme.NutriPlanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NutriPlanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier.weight(1f)) {
                                NavHost(
                                    navController = navController,
                                    startDestination = NavRoutes.MealPlanning.route
                                ) {
                                    composable(NavRoutes.MealPlanning.route) {
                                        MealPlanningScreen()
                                    }
                                    composable(NavRoutes.Recipes.route) {
                                        RecipesScreen()
                                    }
                                    composable(NavRoutes.ShoppingList.route) {
                                        ShoppingListScreen()
                                    }
                                }
                            }

                            BottomNavigationBar(navController = navController)
                        }
                    }
                }
            }
        }
    }
}