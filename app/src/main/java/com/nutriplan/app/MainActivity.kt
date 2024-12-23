package com.nutriplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutriplan.app.data.entities.MealPlanWithRecipes
import com.nutriplan.app.data.entities.Recipe
import com.nutriplan.app.ui.theme.NutriPlanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriPlanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainActivityViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("NutriPlan") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is MainUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is MainUiState.Success -> {
                    val state = uiState as MainUiState.Success
                    MealPlanList(
                        mealPlans = state.mealPlans,
                        onRecipeClick = { recipeId ->
                            viewModel.getRecipeDetails(recipeId)
                        }
                    )

                    state.selectedRecipe?.let { recipe ->
                        RecipeDetailsDialog(
                            recipe = recipe,
                            onDismiss = { viewModel.clearSelectedRecipe() }
                        )
                    }
                }

                is MainUiState.Error -> {
                    ErrorView(
                        message = (uiState as MainUiState.Error).message,
                        onRetry = { /* Add refresh functionality */ }
                    )
                }
            }
        }
    }
}

@Composable
fun MealPlanList(
    mealPlans: List<MealPlanWithRecipes>,
    onRecipeClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mealPlans) { mealPlanWithRecipes ->
            MealPlanCard(
                mealPlanWithRecipes = mealPlanWithRecipes,
                onRecipeClick = onRecipeClick
            )
        }
    }
}

@Composable
fun MealPlanCard(
    mealPlanWithRecipes: MealPlanWithRecipes,
    onRecipeClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Date: ${mealPlanWithRecipes.mealPlan.date}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Breakfast Recipe
            mealPlanWithRecipes.breakfastRecipe?.let { recipe ->
                RecipeRow(
                    mealType = "Breakfast",
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }

            // Lunch Recipe
            mealPlanWithRecipes.lunchRecipe?.let { recipe ->
                RecipeRow(
                    mealType = "Lunch",
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }

            // Dinner Recipe
            mealPlanWithRecipes.dinnerRecipe?.let { recipe ->
                RecipeRow(
                    mealType = "Dinner",
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }

            if (mealPlanWithRecipes.breakfastRecipe == null &&
                mealPlanWithRecipes.lunchRecipe == null &&
                mealPlanWithRecipes.dinnerRecipe == null) {
                Text(
                    text = "No recipes planned",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun RecipeRow(
    mealType: String,
    recipe: Recipe,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = mealType,
            style = MaterialTheme.typography.subtitle1
        )
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(recipe.title)
        }
    }
}

@Composable
fun RecipeDetailsDialog(
    recipe: Recipe,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(recipe.title) },
        text = {
            Column {
                Text("Prep Time: ${recipe.prepTime} minutes")
                Text("Cook Time: ${recipe.cookTime} minutes")
                Text("Servings: ${recipe.servings}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingredients:",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(recipe.ingredients)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Instructions:",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(recipe.instructions)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}