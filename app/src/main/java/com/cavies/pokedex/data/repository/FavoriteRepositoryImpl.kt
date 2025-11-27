package com.cavies.pokedex.data.repository

import com.cavies.pokedex.data.local.room.dao.FavoritesDao
import com.cavies.pokedex.data.local.room.entity.FavoritesEntity
import com.cavies.pokedex.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoritesDao: FavoritesDao
): FavoriteRepository {

    override suspend fun getFavoriteIds(): List<Int> =
        favoritesDao.getAllFavorites().map { it.id }

    override suspend fun addFavorite(id: Int, name: String) =
        favoritesDao.insertFavorite(FavoritesEntity(id, name))


    override suspend fun removeFavorite(id: Int) =
        favoritesDao.deleteFavorite(id)
}