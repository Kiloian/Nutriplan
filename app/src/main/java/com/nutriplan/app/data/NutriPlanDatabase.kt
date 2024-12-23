package com.nutriplan.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nutriplan.app.data.dao.MealPlanDao
import com.nutriplan.app.data.dao.RecipeDao
import com.nutriplan.app.data.dao.ShoppingItemDao
import com.nutriplan.app.data.entities.MealPlan
import com.nutriplan.app.data.entities.Recipe
import com.nutriplan.app.data.entities.ShoppingItem

@Database(
    entities = [
        Recipe::class,
        MealPlan::class,
        ShoppingItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NutriPlanDatabase : RoomDatabase() {

    // Abstract DAO methods
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun shoppingItemDao(): ShoppingItemDao
}