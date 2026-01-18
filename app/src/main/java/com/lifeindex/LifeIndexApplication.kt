package com.lifeindex

import android.app.Application
import com.lifeindex.data.database.AppDatabase

class LifeIndexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize database
        AppDatabase.getDatabase(this)
    }
}
