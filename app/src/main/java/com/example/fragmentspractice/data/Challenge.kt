package com.example.fragmentspractice.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String
)
