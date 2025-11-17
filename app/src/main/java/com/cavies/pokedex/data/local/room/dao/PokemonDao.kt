package com.cavies.pokedex.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.cavies.pokedex.data.local.room.entity.GameWithPokemons
import com.cavies.pokedex.data.local.room.entity.Pokemon

@Dao
interface GameDao {
    @Transaction
    @Query("SELECT * FROM Game WHERE id = :gameId")
    suspend fun getGameWithPokemons(gameId: Int): GameWithPokemons
}

@Dao
interface PokemonDao {
    @Query("SELECT * FROM Pokemon ORDER BY id")
    suspend fun getAllPokemons(): List<Pokemon>
}
