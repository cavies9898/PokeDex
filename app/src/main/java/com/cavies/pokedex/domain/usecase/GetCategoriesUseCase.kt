package com.cavies.pokedex.domain.usecase

import com.cavies.pokedex.domain.model.Categories
import com.cavies.pokedex.domain.repository.CategoriesRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend operator fun invoke(): Categories = repository.getCategories()
}