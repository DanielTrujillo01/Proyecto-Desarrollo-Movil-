package com.example.fragmentspractice.data

import kotlinx.coroutines.flow.Flow

class ChallengeRepository(private val challengeDao: ChallengeDao) {

    val allChallenges: Flow<List<Challenge>> = challengeDao.getAllChallenges()

    suspend fun addChallenge(text: String) {
        challengeDao.insert(Challenge(text = text))
    }

    suspend fun updateChallenge(id: Long, text: String) {
        challengeDao.update(Challenge(id = id, text = text))
    }
}
