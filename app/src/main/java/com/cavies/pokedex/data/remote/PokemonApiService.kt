package com.cavies.pokedex.data.remote

import com.cavies.pokedex.data.remote.model.PokemonDetailResponse
import com.cavies.pokedex.data.remote.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int = 50
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") idPokemon: Int
    ): PokemonDetailResponse
}


