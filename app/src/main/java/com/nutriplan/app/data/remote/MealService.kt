package com.nutriplan.app.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealService {
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): Response<MealSearchResponse>

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): Response<MealDetailsResponse>
}

data class MealSearchResponse(
    val meals: List<RemoteMeal>?
)

data class MealDetailsResponse(
    val meals: List<RemoteMealDetails>?
)

data class RemoteMeal(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strMealThumb: String?
)

data class RemoteMealDetails(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strInstructions: String?,
    val strMealThumb: String?,
    // Add any other fields you need from the API
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    // ... add more ingredients as needed
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?
    // ... add more measurements as needed
)