package com.nutriplan.app.ui.mealplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriplan.app.data.dao.MealPlanDao
import com.nutriplan.app.data.dao.RecipeDao
import com.nutriplan.app.data.entities.MealPlan
import com.nutriplan.app.data.entities.MealPlanWithRecipes
import com.nutriplan.app.data.entities.Recipe
import com.nutriplan.app.data.entities.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val mealPlanDao: MealPlanDao,
    private val recipeDao: RecipeDao
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    val weeklyMealPlans: StateFlow<Map<String, MealPlanWithRecipes>> =
        _selectedDate.flatMapLatest { date ->
            val weekStart = date.with(DayOfWeek.MONDAY)
            val weekEnd = weekStart.plusDays(6)
            mealPlanDao.getMealPlansForDateRange(
                weekStart.toString(),
                weekEnd.toString()
            ).map { mealPlans ->
                mealPlans.associateBy { it.mealPlan.date }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    init {
        viewModelScope.launch {
            recipeDao.getAllRecipes().collect { recipeList ->
                _recipes.value = recipeList
            }
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun assignRecipe(date: LocalDate, mealType: MealType, recipeId: Long?) {
        viewModelScope.launch {
            val dateStr = date.toString()
            val existingPlan = weeklyMealPlans.value[dateStr]?.mealPlan

            val updatedPlan = when (existingPlan) {
                null -> MealPlan(
                    date = dateStr,
                    breakfastRecipeId = if (mealType == MealType.BREAKFAST) recipeId else null,
                    lunchRecipeId = if (mealType == MealType.LUNCH) recipeId else null,
                    dinnerRecipeId = if (mealType == MealType.DINNER) recipeId else null
                )
                else -> existingPlan.copy(
                    breakfastRecipeId = if (mealType == MealType.BREAKFAST) recipeId else existingPlan.breakfastRecipeId,
                    lunchRecipeId = if (mealType == MealType.LUNCH) recipeId else existingPlan.lunchRecipeId,
                    dinnerRecipeId = if (mealType == MealType.DINNER) recipeId else existingPlan.dinnerRecipeId
                )
            }

            if (existingPlan == null) {
                mealPlanDao.insertMealPlan(updatedPlan)
            } else {
                mealPlanDao.updateMealPlan(updatedPlan)
            }
        }
    }
}