package com.cavies.pokedex.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey
    @ColumnInfo("id") val id: Int = 0,
    @ColumnInfo("name") val name: String = ""
)