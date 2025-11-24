package com.cavies.pokedex.domain.repository

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.presentation.ui.home.Categories

interface PokemonRepository {

    // --- SQLite ---
    suspend fun getPokemons(): List<Pokemon>
    suspend fun getCategories(): Categories

    // --- Room ---
    suspend fun getFavoriteIds(): List<Int>
    suspend fun addFavorite(id: Int, name: String)
    suspend fun removeFavorite(id: Int)
}


