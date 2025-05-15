package com.example.robokalamassignmentapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PortfolioEntry::class], version = 1)
abstract class PortfolioDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao

    companion object {
        @Volatile
        private var INSTANCE: PortfolioDatabase? = null

        fun getDatabase(context: Context): PortfolioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PortfolioDatabase::class.java,
                    "portfolio_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
