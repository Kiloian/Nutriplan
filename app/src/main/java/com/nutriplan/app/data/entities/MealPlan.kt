package com.nutriplan.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,  // Keeping String for now
    val breakfastRecipeId: Long? = null,
    val lunchRecipeId: Long? = null,
    val dinnerRecipeId: Long? = null
)