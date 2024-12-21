package com.nutriplan.app.navigation

sealed class NavRoutes(val route: String) {
    object MealPlanning : NavRoutes("meal_planning")
    object Recipes : NavRoutes("recipes")
    object ShoppingList : NavRoutes("shopping_list")
}