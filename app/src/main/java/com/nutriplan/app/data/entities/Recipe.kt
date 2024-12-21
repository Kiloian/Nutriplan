package com.nutriplan.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val prepTime: Int,
    val cookTime: Int,
    val servings: Int,
    val ingredients: String,
    val instructions: String
)