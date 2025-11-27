package com.cavies.pokedex.data.repository

import com.cavies.pokedex.data.local.room.dao.FavoritesDao
import com.cavies.pokedex.data.local.room.entity.FavoritesEntity
import com.cavies.pokedex.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoritesDao
) : FavoriteRepository {

    override fun getFavoriteIdsFlow(): Flow<List<Int>> =
        dao.getFavoriteIdsFlow()

    override suspend fun addFavorite(id: Int, name: String) =
        dao.insertFavorite(FavoritesEntity(id, name))

    override suspend fun removeFavorite(id: Int) =
        dao.deleteFavorite(id)
}
