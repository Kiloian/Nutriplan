package com.nutriplan.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriplan.app.data.repository.MealPlanRepository
import com.nutriplan.app.data.entities.Recipe
import com.nutriplan.app.data.entities.MealPlanWithRecipes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(
        val mealPlans: List<MealPlanWithRecipes>,
        val selectedRecipe: Recipe?
    ) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MealPlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _mealPlans = MutableStateFlow<List<MealPlanWithRecipes>>(emptyList())
    val mealPlans: StateFlow<List<MealPlanWithRecipes>> = _mealPlans.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        getMealPlansForCurrentWeek()
    }

    private fun getMealPlansForCurrentWeek() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            try {
                val startDate = LocalDate.now()
                repository.getMealPlansForWeek(startDate).collect { mealPlans ->
                    _mealPlans.value = mealPlans
                    _uiState.value = MainUiState.Success(mealPlans, _selectedRecipe.value)
                }
            } catch (e: Exception) {
                _isError.value = true
                _snackbarMessage.emit("Error loading meal plans: ${e.localizedMessage}")
                _uiState.value = MainUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipeDetails(recipeId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recipe = _mealPlans.value
                    .flatMap { it.recipes }
                    .find { it.id == recipeId }
                _selectedRecipe.value = recipe
                _uiState.value = MainUiState.Success(_mealPlans.value, recipe)
            } catch (e: Exception) {
                _snackbarMessage.emit("Error loading recipe details: ${e.localizedMessage}")
                _uiState.value = MainUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedRecipe() {
        _selectedRecipe.value = null
        _uiState.value = MainUiState.Success(_mealPlans.value, null)
    }
}