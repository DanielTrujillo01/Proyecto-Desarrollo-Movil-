package com.example.fragmentspractice.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    @Query("SELECT * FROM challenges ORDER BY id DESC")
    fun getAllChallenges(): Flow<List<Challenge>>

    @Insert
    suspend fun insert(challenge: Challenge)

    @Update
    suspend fun update(challenge: Challenge)
}
