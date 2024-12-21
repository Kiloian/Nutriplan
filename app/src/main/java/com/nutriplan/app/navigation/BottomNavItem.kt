package com.nutriplan.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object MealPlanning : BottomNavItem(
        route = NavRoutes.MealPlanning.route,
        title = "Meal Plan",
        icon = Icons.Default.CalendarToday
    )
    object Recipes : BottomNavItem(
        route = NavRoutes.Recipes.route,
        title = "Recipes",
        icon = Icons.Default.MenuBook
    )
    object ShoppingList : BottomNavItem(
        route = NavRoutes.ShoppingList.route,
        title = "Shopping",
        icon = Icons.Default.ShoppingCart
    )
}