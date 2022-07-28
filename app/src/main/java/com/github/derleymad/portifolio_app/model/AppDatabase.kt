package com.github.derleymad.portifolio_app.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BioFav::class], version = 1)
//Padrão Singleton esse objeto aki em baixo vai ser ÚNICO pois será armazenado em memória...

abstract class AppDatabase : RoomDatabase() {
    abstract fun favDao(): FavDao

    companion object{
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "repo_db"
                    ).build()
                }
                INSTANCE as AppDatabase
            }else{
                INSTANCE as AppDatabase
            }
        }
    }
}