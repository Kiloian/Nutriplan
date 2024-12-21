package com.nutriplan.app.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MealPlanWithRecipes(
    @Embedded val mealPlan: MealPlan,
    @Relation(
        parentColumn = "breakfastRecipeId",
        entityColumn = "id"
    )
    val breakfastRecipe: Recipe?,
    @Relation(
        parentColumn = "lunchRecipeId",
        entityColumn = "id"
    )
    val lunchRecipe: Recipe?,
    @Relation(
        parentColumn = "dinnerRecipeId",
        entityColumn = "id"
    )
    val dinnerRecipe: Recipe?
)