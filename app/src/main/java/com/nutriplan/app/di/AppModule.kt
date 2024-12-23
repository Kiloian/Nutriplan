package com.nutriplan.app.di

import android.content.Context
import androidx.room.Room
import com.nutriplan.app.data.NutriPlanDatabase
import com.nutriplan.app.data.dao.MealPlanDao
import com.nutriplan.app.data.dao.RecipeDao
import com.nutriplan.app.data.dao.ShoppingItemDao
import com.nutriplan.app.data.remote.MealService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provide the Room database
    @Provides
    @Singleton
    fun provideNutriPlanDatabase(@ApplicationContext context: Context): NutriPlanDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NutriPlanDatabase::class.java,
            "nutriplan_database"
        )
            .fallbackToDestructiveMigration() // Use proper migrations in production
            .build()
    }

    // Provide DAOs
    @Provides
    fun provideMealPlanDao(database: NutriPlanDatabase): MealPlanDao {
        return database.mealPlanDao()
    }

    @Provides
    fun provideRecipeDao(database: NutriPlanDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun provideShoppingItemDao(database: NutriPlanDatabase): ShoppingItemDao {
        return database.shoppingItemDao()
    }

    // Provide Retrofit service
    @Provides
    @Singleton
    fun provideMealService(): MealService {
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealService::class.java)
    }
}