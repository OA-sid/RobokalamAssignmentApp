package com.example.robokalamassignmentapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PortfolioDao {
    @Insert
    suspend fun insert(entry: PortfolioEntry)

    @Query("SELECT * FROM portfolio_entries WHERE userId = :userId")
    suspend fun getEntriesForUser(userId: String): List<PortfolioEntry>

    @Query("SELECT * FROM portfolio_entries")
    suspend fun getAllEntries(): List<PortfolioEntry>

    @Delete
    suspend fun deleteEntry(entry: PortfolioEntry)
}