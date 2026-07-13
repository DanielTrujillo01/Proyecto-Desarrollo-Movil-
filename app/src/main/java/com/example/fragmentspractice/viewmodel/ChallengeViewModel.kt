package com.example.fragmentspractice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentspractice.data.AppDatabase
import com.example.fragmentspractice.data.Challenge
import com.example.fragmentspractice.data.ChallengeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChallengeRepository =
        ChallengeRepository(AppDatabase.getInstance(application).challengeDao())

    val challenges: StateFlow<List<Challenge>> = repository.allChallenges.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addChallenge(text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isEmpty()) return
        viewModelScope.launch {
            repository.addChallenge(trimmedText)
        }
    }

    fun updateChallenge(id: Long, text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isEmpty()) return
        viewModelScope.launch {
            repository.updateChallenge(id, trimmedText)
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.deleteChallenge(challenge)
        }
    }
}
