package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonsUseCase @Inject constructor(
    private val repo: PokemonRepository
) {
    suspend operator fun invoke(): List<Pokemon> {
        val base = repo.getPokemons()
        val favorites = repo.getFavoriteIds().toSet()

        return base.map { p ->
            p.copy(
                isFavorite = favorites.contains(p.id)
            )
        }
    }
}