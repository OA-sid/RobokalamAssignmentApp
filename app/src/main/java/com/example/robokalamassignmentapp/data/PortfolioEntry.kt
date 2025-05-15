package com.example.robokalamassignmentapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio_entries")
data class PortfolioEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val college: String,
    val skills: String,
    val projectTitle: String,
    val projectDescription: String,
    val userId: String
)