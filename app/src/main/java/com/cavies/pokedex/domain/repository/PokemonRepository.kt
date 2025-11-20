package com.cavies.pokedex.domain.repository

import com.cavies.pokedex.domain.model.Pokemon

interface PokemonRepository {

    // --- SQLite ---
    suspend fun getPokemonsFromDb(): List<Pokemon>
    suspend fun getTypesFromDb(): List<String>
    suspend fun getTypesForPokemon(id: Int): List<String>

    // --- API ---
    suspend fun getPokemonImageUrl(id: Int): String?

    // --- Room ---
    suspend fun getFavoriteIds(): List<Int>
    suspend fun addFavorite(id: Int, name: String)
    suspend fun removeFavorite(id: Int)
}


