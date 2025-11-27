package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.FavoriteRepository
import com.cavies.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repoPokemon: PokemonRepository,
    private val repoFavorites: FavoriteRepository
) {
    operator fun invoke(): Flow<List<Pokemon>> =
        repoFavorites.getFavoriteIdsFlow().transform { ids ->
            val favorites = ids.mapNotNull { id ->
                repoPokemon.getPokemonById(id)?.copy(isFavorite = true)
            }
            emit(favorites)
        }
}