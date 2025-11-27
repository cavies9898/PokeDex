package com.cavies.pokedex.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteIdsFlow(): Flow<List<Int>>
    suspend fun addFavorite(id: Int, name: String)
    suspend fun removeFavorite(id: Int)
}