package com.cavies.pokedex.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cavies.pokedex.data.local.room.dao.FavoritesDao
import com.cavies.pokedex.data.local.room.entity.FavoritesEntity

@Database(
    entities = [FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
