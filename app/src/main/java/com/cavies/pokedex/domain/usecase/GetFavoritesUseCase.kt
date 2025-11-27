package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.FavoriteRepository
import com.cavies.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repoPokemon: PokemonRepository,
    private val repoFavorites: FavoriteRepository
) {
    suspend operator fun invoke(): List<Pokemon> {
        val favoriteIds = repoFavorites.getFavoriteIds()
        return favoriteIds.mapNotNull { id ->
            repoPokemon.getPokemonById(id)?.copy(isFavorite = true)
        }
    }
}
