package com.cavies.pokedex.data.repository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cavies.pokedex.data.local.room.dao.FavoritesDao
import com.cavies.pokedex.data.local.room.db.AppDatabase
import com.cavies.pokedex.data.local.room.db.DatabaseHelper
import com.cavies.pokedex.data.local.room.entity.FavoritesEntity
import com.cavies.pokedex.data.remote.PokemonApiService
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.PokemonRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val sqliteDb: SQLiteDatabase,
    private val favoritesDao: FavoritesDao,
    private val api: PokemonApiService
) : PokemonRepository {

    // -----------------------------------------------------------------------------------------
    // 1. GET POKEMONES desde SQLite (SOLO id + name)
    // -----------------------------------------------------------------------------------------
    override suspend fun getPokemonsFromDb(): List<Pokemon> = withContext(Dispatchers.IO) {
        val result = mutableListOf<Pokemon>()

        val cursor = sqliteDb.rawQuery(
            "SELECT id, identifier FROM pokemon",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)

                result.add(
                    Pokemon(
                        id = id,
                        name = name,
                        types = emptyList(),
                        imageUrl = null,
                        isFavorite = false
                    )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        result
    }

    // -----------------------------------------------------------------------------------------
    // 2. GET ALL TYPES desde SQLite (para filtros)
    // -----------------------------------------------------------------------------------------
    override suspend fun getTypesFromDb(): List<String> = withContext(Dispatchers.IO) {
        val list = mutableListOf<String>()

        val cursor = sqliteDb.rawQuery(
            "SELECT identifier FROM types ORDER BY id",
            null
        )

        if (cursor.moveToFirst()) {
            do list.add(cursor.getString(0)) while (cursor.moveToNext())
        }

        cursor.close()
        list
    }

    // -----------------------------------------------------------------------------------------
    // 3. GET TYPES FOR POKEMON desde SQLite
    // -----------------------------------------------------------------------------------------
    override suspend fun getTypesForPokemon(id: Int): List<String> = withContext(Dispatchers.IO) {
        val list = mutableListOf<String>()

        val cursor = sqliteDb.rawQuery(
            """
            SELECT t.identifier
            FROM pokemon_types pt
            JOIN types t ON t.id = pt.type_id
            WHERE pt.pokemon_id = ?
            ORDER BY pt.slot
            """.trimIndent(),
            arrayOf(id.toString())
        )

        if (cursor.moveToFirst()) {
            do list.add(cursor.getString(0)) while (cursor.moveToNext())
        }

        cursor.close()
        list
    }

    // -----------------------------------------------------------------------------------------
    // 4. API → Obtener la imagen
    // -----------------------------------------------------------------------------------------
    override suspend fun getPokemonImageUrl(id: Int): String? {
        return try {
            api.getPokemonDetail(id).sprites.others.officialArtwork.imageUrl
        } catch (e: Exception) {
            null
        }
    }

    // -----------------------------------------------------------------------------------------
    // 5. ROOM → Obtener todos los favoritos
    // -----------------------------------------------------------------------------------------
    override suspend fun getFavoriteIds(): List<Int> {
        return favoritesDao.getAllFavorites().map { it.id }
    }

    // -----------------------------------------------------------------------------------------
    // 6. ROOM → Agregar favorito
    // -----------------------------------------------------------------------------------------
    override suspend fun addFavorite(id: Int, name: String) {
        favoritesDao.insertFavorite(FavoritesEntity(id, name))
    }

    // -----------------------------------------------------------------------------------------
    // 7. ROOM → Eliminar favorito
    // -----------------------------------------------------------------------------------------
    override suspend fun removeFavorite(id: Int) {
        favoritesDao.deleteFavorite(id)
    }
}
