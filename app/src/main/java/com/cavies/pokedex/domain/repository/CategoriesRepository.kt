package com.cavies.pokedex.domain.repository

import com.cavies.pokedex.domain.model.Categories

interface CategoriesRepository {
    suspend fun getCategories(): Categories
}