package com.cavies.pokedex.domain.repository

import com.cavies.pokedex.domain.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemons(): List<Pokemon>
    suspend fun getPokemonById(id: Int): Pokemon?
}


