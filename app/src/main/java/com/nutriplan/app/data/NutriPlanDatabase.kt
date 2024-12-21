package com.nutriplan.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nutriplan.app.data.entities.Recipe
import com.nutriplan.app.data.entities.MealPlan
import com.nutriplan.app.data.entities.ShoppingItem
import com.nutriplan.app.data.dao.RecipeDao
import com.nutriplan.app.data.dao.MealPlanDao
import com.nutriplan.app.data.dao.ShoppingItemDao

@Database(
    entities = [Recipe::class, MealPlan::class, ShoppingItem::class],
    version = 1,              // This must be a literal number
    exportSchema = false      // This must be a literal boolean
)
@TypeConverters(Converters::class)
abstract class NutriPlanDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        private const val DATABASE_VERSION = 1  // If you want to define it as a constant

        @Volatile
        private var Instance: NutriPlanDatabase? = null

        fun getDatabase(context: Context): NutriPlanDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    NutriPlanDatabase::class.java,
                    "nutriplan_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}