package com.example.fragmentspractice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentspractice.data.Pokemon
import com.example.fragmentspractice.data.PokemonRepository
import kotlinx.coroutines.launch

class ChallengeResultViewModel: ViewModel() {

    private val repository = PokemonRepository()

    private val _pokemon = MutableLiveData<Pokemon?>()
    val pokemon: LiveData<Pokemon?> get() = _pokemon

    fun loadRandomPokemon() {
        viewModelScope.launch {
            _pokemon.value = repository.getRandomPokemon()
        }
    }
}