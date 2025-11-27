package com.cavies.pokedex.domain.repository

interface FavoriteRepository {
    suspend fun getFavoriteIds(): List<Int>
    suspend fun addFavorite(id: Int, name: String)
    suspend fun removeFavorite(id: Int)
}