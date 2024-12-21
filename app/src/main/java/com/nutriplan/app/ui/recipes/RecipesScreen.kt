package com.nutriplan.app.ui.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.nutriplan.app.data.entities.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    viewModel: RecipeViewModel = hiltViewModel()
) {
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    val recipes by viewModel.allRecipes.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
            }
        }
    ) { paddingValues ->
        if (selectedRecipe != null) {
            RecipeDetailScreen(
                recipe = selectedRecipe!!,
                onBack = { selectedRecipe = null }
            )
        } else {
            RecipeList(
                recipes = recipes,
                onRecipeClick = { selectedRecipe = it },
                modifier = Modifier.padding(paddingValues)
            )
        }

        if (showAddDialog) {
            AddRecipeDialog(
                onDismiss = { showAddDialog = false },
                onAddRecipe = { recipe ->
                    viewModel.addRecipe(recipe)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun RecipeList(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes) { recipe ->
            RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RecipeInfo(label = "Prep", value = "${recipe.prepTime} min")
                RecipeInfo(label = "Cook", value = "${recipe.cookTime} min")
                RecipeInfo(label = "Servings", value = recipe.servings.toString())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRecipeDialog(
    onDismiss: () -> Unit,
    onAddRecipe: (Recipe) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var prepTime by remember { mutableStateOf("") }
    var cookTime by remember { mutableStateOf("") }
    var servings by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Recipe") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Recipe Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = prepTime,
                    onValueChange = { prepTime = it },
                    label = { Text("Prep Time (minutes)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cookTime,
                    onValueChange = { cookTime = it },
                    label = { Text("Cook Time (minutes)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = servings,
                    onValueChange = { servings = it },
                    label = { Text("Servings") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    label = { Text("Ingredients") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        val recipe = Recipe(
                            title = title,
                            prepTime = prepTime.toIntOrNull() ?: 0,
                            cookTime = cookTime.toIntOrNull() ?: 0,
                            servings = servings.toIntOrNull() ?: 0,
                            ingredients = ingredients,
                            instructions = instructions
                        )
                        onAddRecipe(recipe)
                    }
                },
                enabled = title.isNotBlank() &&
                        prepTime.isNotBlank() &&
                        cookTime.isNotBlank() &&
                        servings.isNotBlank()
            ) {
                Text("Add Recipe")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun RecipeDetailScreen(
    recipe: Recipe,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = recipe.ingredients)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = recipe.instructions)
    }
}

@Composable
private fun RecipeInfo(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}