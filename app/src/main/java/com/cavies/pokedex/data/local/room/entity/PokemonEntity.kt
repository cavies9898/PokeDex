package com.cavies.pokedex.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val generation: Int
)

@Entity
data class Game(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(primaryKeys = ["pokemonId", "gameId"],
    indices = [Index(value = ["gameId"])])
data class PokemonGameCrossRef(
    val pokemonId: Int,
    val gameId: Int
)


data class GameWithPokemons(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            PokemonGameCrossRef::class,
            parentColumn = "gameId",
            entityColumn = "pokemonId"
        )
    )
    val pokemons: List<Pokemon>
)
