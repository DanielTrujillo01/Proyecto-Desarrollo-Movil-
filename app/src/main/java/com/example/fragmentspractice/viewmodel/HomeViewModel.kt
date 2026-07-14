package com.example.fragmentspractice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private var _currentAngle = 0f
    val currentAngle: Float get() = _currentAngle

    private val _showChallengeEvent = MutableLiveData<Boolean>()
    val showChallengeEvent: LiveData<Boolean> get() = _showChallengeEvent

    fun updateAngle(newAngle: Float) {
        _currentAngle = newAngle % 360
    }

    fun triggerChallenge() {
        _showChallengeEvent.value = true
    }

    fun onChallengeShown() {
        _showChallengeEvent.value = false
    }
}