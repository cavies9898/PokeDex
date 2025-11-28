package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.FavoriteRepository
import com.cavies.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPokemonsUseCase @Inject constructor(
    private val repoPokemon: PokemonRepository,
    private val repoFavorites: FavoriteRepository
) {

    operator fun invoke(): Flow<List<Pokemon>> = flow {
        val baseList = repoPokemon.getPokemons()
        emit(baseList)
        repoFavorites.getFavoriteIdsFlow().collect { favoriteIds ->
            val favSet = favoriteIds.toSet()
            emit(
                baseList.map { p ->
                    p.copy(isFavorite = favSet.contains(p.id))
                }
            )
        }
    }
}
