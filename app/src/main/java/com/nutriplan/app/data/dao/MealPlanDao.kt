package com.nutriplan.app.data.dao

import androidx.room.*
import com.nutriplan.app.data.entities.MealPlan
import com.nutriplan.app.data.entities.MealPlanWithRecipes
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    @Transaction
    @Query("SELECT * FROM meal_plans WHERE date BETWEEN :startDate AND :endDate")
    fun getMealPlansForDateRange(startDate: String, endDate: String): Flow<List<MealPlanWithRecipes>>

    @Transaction
    @Query("SELECT * FROM meal_plans")
    fun getAllMealPlans(): Flow<List<MealPlanWithRecipes>>

    @Transaction
    @Query("SELECT * FROM meal_plans WHERE date = :date")
    fun getMealPlanForDate(date: String): Flow<MealPlanWithRecipes?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlan)

    @Update
    suspend fun updateMealPlan(mealPlan: MealPlan)

    @Delete
    suspend fun deleteMealPlan(mealPlan: MealPlan)
}