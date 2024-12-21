package com.nutriplan.app.data.repository

import com.nutriplan.app.data.dao.MealPlanDao
import com.nutriplan.app.data.entities.MealPlan
import com.nutriplan.app.data.entities.MealPlanWithRecipes
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class MealPlanRepository(private val mealPlanDao: MealPlanDao) {
    fun getMealPlansForWeek(startDate: LocalDate): Flow<List<MealPlanWithRecipes>> {
        val endDate = startDate.plusDays(6)
        return mealPlanDao.getMealPlansForDateRange(
            startDate.toString(),
            endDate.toString()
        )
    }

    suspend fun insertMealPlan(mealPlan: MealPlan) = mealPlanDao.insertMealPlan(mealPlan)
    suspend fun updateMealPlan(mealPlan: MealPlan) = mealPlanDao.updateMealPlan(mealPlan)
    suspend fun deleteMealPlan(mealPlan: MealPlan) = mealPlanDao.deleteMealPlan(mealPlan)

    fun getMealPlanForDate(date: LocalDate): Flow<MealPlanWithRecipes?> {
        return mealPlanDao.getMealPlanForDate(date.toString())
    }
}