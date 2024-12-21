package com.nutriplan.app.data.repository

import com.nutriplan.app.data.dao.RecipeDao
import com.nutriplan.app.data.entities.Recipe
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()

    suspend fun insertRecipe(recipe: Recipe) = recipeDao.insertRecipe(recipe)
    suspend fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)
    suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)
    fun getRecipeById(id: Long): Flow<Recipe> = recipeDao.getRecipeById(id)
}