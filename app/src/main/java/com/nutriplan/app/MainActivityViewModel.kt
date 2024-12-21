package com.nutriplan.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriplan.app.data.MealRepository
import com.nutriplan.app.data.model.Meal
import com.nutriplan.app.data.model.MealDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val meals: List<Meal>, val selectedMeal: MealDetails?) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _selectedMeal = MutableStateFlow<MealDetails?>(null)
    val selectedMeal: StateFlow<MealDetails?> = _selectedMeal.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        getMeals()
    }

    private fun getMeals() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            try {
                val response = repository.getMeals()
                if (response.isSuccessful) {
                    _meals.value = response.body()?.meals ?: emptyList()
                } else {
                    _isError.value = true
                    _snackbarMessage.emit("Error fetching meals: ${response.message()}")
                }
            } catch (e: Exception) {
                _isError.value = true
                _snackbarMessage.emit("Network error: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getMealDetails(id: String) {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading // Set loading state
            try {
                val response = repository.getMealDetails(id)
                if (response.isSuccessful) {
                    val mealDetails = response.body()?.meals?.firstOrNull()
                    _uiState.value = MainUiState.Success(meals = _meals.value, selectedMeal = mealDetails)
                } else {
                    _uiState.value = MainUiState.Error("API Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = MainUiState.Error("Network Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedMeal() {
        _selectedMeal.value = null
    }
}