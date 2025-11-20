package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonsUseCase @Inject constructor(
    private val repo: PokemonRepository
) {
    suspend operator fun invoke(): List<Pokemon> {

        val base = repo.getPokemonsFromDb()
        val favorites = repo.getFavoriteIds()

        return base.map { p ->

            val types = repo.getTypesForPokemon(p.id)
            val imageUrl = repo.getPokemonImageUrl(p.id)

            Pokemon(
                id = p.id,
                name = p.name,
                types = types,
                imageUrl = imageUrl,
                isFavorite = p.id in favorites,
            )
        }
    }
}

class ToggleFavoriteUseCase @Inject constructor(
    private val repo: PokemonRepository
) {
    suspend operator fun invoke(pokemon: Pokemon) {
        if (pokemon.isFavorite) repo.removeFavorite(pokemon.id)
        else repo.addFavorite(pokemon.id, pokemon.name)
    }
}

class GetTypesUseCase @Inject constructor(
    private val repo: PokemonRepository
) {
    suspend operator fun invoke(): List<String> = repo.getTypesFromDb()
}

class FilterByTypeUseCase @Inject constructor() {
    operator fun invoke(all: List<Pokemon>, type: String): List<Pokemon> {
        return all.filter { type in it.types }
    }
}
