package com.nutriplan.app.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriplan.app.data.dao.RecipeDao
import com.nutriplan.app.data.entities.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeDao: RecipeDao
) : ViewModel() {
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.insertRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.deleteRecipe(recipe)
        }
    }
}