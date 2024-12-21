package com.nutriplan.app.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.nutriplan.app.ui.theme.NutriPlanTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriPlanTheme {
                MainScreen(viewModel)
            }
        }

        // Observe the Snackbar message from the ViewModel
        lifecycleScope.launchWhenStarted {
            viewModel.snackbarMessage.collect { message ->
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainActivityViewModel) {
    val meals by viewModel.meals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isError by viewModel.isError.collectAsState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("NutriPlan") }) }
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (isError) {
            Text("Error loading data")
        } else {
            MealList(meals = meals) { mealId ->
                viewModel.getMealDetails(mealId)
            }
        }
    }

    val selectedMeal by viewModel.selectedMeal.collectAsState()
    selectedMeal?.let { meal ->
        MealDetailsScreen(meal = meal) {
            viewModel.clearSelectedMeal()
        }
    }
}