package com.nutriplan.app

import android.app.Application
import com.nutriplan.app.data.NutriPlanDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NutriPlanApplication : Application() {
    // We can keep this for backward compatibility during migration
    val database: NutriPlanDatabase by lazy {
        NutriPlanDatabase.getDatabase(this)
    }
}