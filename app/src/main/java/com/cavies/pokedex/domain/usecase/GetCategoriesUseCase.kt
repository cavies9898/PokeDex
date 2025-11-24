package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.repository.PokemonRepository
import com.cavies.pokedex.presentation.ui.home.Categories
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): Categories = repository.getCategories()
}