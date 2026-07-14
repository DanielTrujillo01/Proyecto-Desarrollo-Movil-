package com.example.fragmentspractice.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class PokemonRepository {

    suspend fun getRandomPokemon(): Pokemon? = withContext(Dispatchers.IO) {
        try {
            val connection = (URL(POKEDEX_URL).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 8000
                readTimeout = 8000
            }

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()

            val pokemonArray = JSONObject(response).getJSONArray("pokemon")
            if (pokemonArray.length() == 0) return@withContext null

            val randomIndex = (0 until pokemonArray.length()).random()
            val pokemonJson = pokemonArray.getJSONObject(randomIndex)

            Pokemon(
                name = pokemonJson.getString("name"),
                // La API entrega las imágenes con http://, se fuerza https:// para
                // evitar el bloqueo de tráfico "cleartext" en Android 9+.
                imageUrl = pokemonJson.getString("img").replace("http://", "https://")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo pokémon aleatorio", e)
            null
        }
    }

    companion object {
        private const val TAG = "PokemonRepository"
        private const val POKEDEX_URL =
            "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json"
    }
}