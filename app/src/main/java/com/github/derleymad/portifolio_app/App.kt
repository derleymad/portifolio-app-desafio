package com.github.derleymad.portifolio_app

import android.app.Application
import com.github.derleymad.portifolio_app.model.AppDatabase

class App : Application(){
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this@App)
    }
}