package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject


class ToggleFavoriteUseCase @Inject constructor(
    private val repo: PokemonRepository
) {
    suspend operator fun invoke(pokemon: Pokemon) {
        if (pokemon.isFavorite) repo.removeFavorite(pokemon.id)
        else repo.addFavorite(pokemon.id, pokemon.name)
    }
}