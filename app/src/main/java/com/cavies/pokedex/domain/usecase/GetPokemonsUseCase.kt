package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): List<Pokemon> = repository.getPokemons()
}

