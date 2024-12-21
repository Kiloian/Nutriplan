package com.nutriplan.app.di

import android.content.Context
import androidx.room.Room
import com.nutriplan.app.data.dao.MealDao
import com.nutriplan.app.data.dao.MealPlanDao
import com.nutriplan.app.data.local.MealDatabase
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

    @Provides
    @Singleton
    fun provideMealDatabase(@ApplicationContext context: Context): MealDatabase {
        return Room.databaseBuilder(
            context,
            MealDatabase::class.java,
            "meal_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMealPlanDao(database: MealDatabase): MealPlanDao {
        return database.mealPlanDao()
    }

    @Provides
    @Singleton
    fun provideMealDao(database: MealDatabase): MealDao {
        return database.mealDao()
    }

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