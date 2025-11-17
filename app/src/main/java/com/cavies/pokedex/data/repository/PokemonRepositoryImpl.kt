package com.cavies.pokedex.data.repository

import com.cavies.pokedex.data.remote.PokemonApiService
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApiService
) : PokemonRepository {

    override suspend fun getPokemons(): List<Pokemon> {
        val listResponse = api.getPokemons()

        return coroutineScope {
            listResponse.results.map { dto ->
                async {
                    val id = extractIdFromUrl(dto.url)
                    val detail = api.getPokemon(id)
                    Pokemon(
                        id = detail.id,
                        name = detail.name,
                        types = detail.types.map { it.type.name },
                        imageUrl = detail.sprites.others.officialArtwork.imageUrl
                    )
                }
            }.awaitAll()
        }
    }


    private fun extractIdFromUrl(url: String): Int {
        return url.trimEnd('/').split("/").last().toInt()
    }
}

