package com.nutriplan.app.ui.mealplan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nutriplan.app.data.entities.Recipe
import com.nutriplan.app.data.entities.MealType
import com.nutriplan.app.data.entities.MealPlanWithRecipes
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MealPlanningScreen(
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val weeklyMealPlans by viewModel.weeklyMealPlans.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WeekSelector(
            selectedDate = selectedDate,
            onDateSelected = { viewModel.updateSelectedDate(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(
                weeklyMealPlans.entries.sortedBy { it.key }
            ) { (dateStr, mealPlan) ->
                val date = LocalDate.parse(dateStr)
                DayMealPlanCard(
                    date = date,
                    mealPlan = mealPlan,
                    isSelected = dateStr == selectedDate.toString(),
                    recipes = recipes,
                    onAssignRecipe = { mealType, recipeId ->
                        scope.launch {
                            viewModel.assignRecipe(date, mealType, recipeId)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun WeekSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onDateSelected(selectedDate.minusWeeks(1)) }) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous Week")
        }

        Text(
            text = "Week of ${selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}",
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(onClick = { onDateSelected(selectedDate.plusWeeks(1)) }) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next Week")
        }
    }
}

@Composable
private fun DayMealPlanCard(
    date: LocalDate,
    mealPlan: MealPlanWithRecipes?,
    isSelected: Boolean,
    recipes: List<Recipe>,
    onAssignRecipe: (MealType, Long?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = date.format(DateTimeFormatter.ofPattern("EEEE, MMM d")),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            MealSlot(
                label = "Breakfast",
                recipe = mealPlan?.breakfastRecipe,
                recipes = recipes,
                onAssignRecipe = { recipeId -> onAssignRecipe(MealType.BREAKFAST, recipeId) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            MealSlot(
                label = "Lunch",
                recipe = mealPlan?.lunchRecipe,
                recipes = recipes,
                onAssignRecipe = { recipeId -> onAssignRecipe(MealType.LUNCH, recipeId) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            MealSlot(
                label = "Dinner",
                recipe = mealPlan?.dinnerRecipe,
                recipes = recipes,
                onAssignRecipe = { recipeId -> onAssignRecipe(MealType.DINNER, recipeId) }
            )
        }
    }
}

@Composable
private fun MealSlot(
    label: String,
    recipe: Recipe?,
    recipes: List<Recipe>,
    onAssignRecipe: (Long?) -> Unit
) {
    var showRecipeDialog by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showRecipeDialog = true }
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recipe?.title ?: "Add Recipe",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (recipe != null) {
                    IconButton(
                        onClick = { onAssignRecipe(null) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Remove Recipe"
                        )
                    }
                }
            }
        }
    }

    if (showRecipeDialog) {
        RecipeSelectionDialog(
            recipes = recipes,
            onRecipeSelected = { recipeId ->
                onAssignRecipe(recipeId)
                showRecipeDialog = false
            },
            onDismiss = { showRecipeDialog = false }
        )
    }
}

@Composable
private fun RecipeSelectionDialog(
    recipes: List<Recipe>,
    onRecipeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Recipe") },
        text = {
            LazyColumn {
                items(recipes) { recipe ->
                    Text(
                        text = recipe.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRecipeSelected(recipe.id) }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}