package com.cavies.pokedex.data.repository

import android.database.sqlite.SQLiteDatabase
import com.cavies.pokedex.domain.model.Categories
import com.cavies.pokedex.domain.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val sqliteDb: SQLiteDatabase
) : CategoriesRepository {

    override suspend fun getCategories(): Categories = withContext(Dispatchers.IO) {

        val type = mutableListOf<String>()
        val generation = mutableListOf<String>()
        val region = mutableListOf<String>()
        val habitat = mutableListOf<String>()
        val color = mutableListOf<String>()
        val shape = mutableListOf<String>()

        val cursor = sqliteDb.rawQuery(
            """
        SELECT 'type' AS category_name, t.identifier AS value
        FROM types t
        
        UNION
        
        SELECT 'generation', g.identifier
        FROM generations g
        
        UNION
        
        SELECT 'region', rn.name
        FROM region_names rn
        WHERE rn.local_language_id = 9
        
        UNION
        
        SELECT 'habitat', h.identifier
        FROM pokemon_habitats h
        
        UNION
        
        SELECT 'color', c.identifier
        FROM pokemon_colors c
        
        UNION
        
        SELECT 'shape', s.identifier
        FROM pokemon_shapes s;
        """.trimIndent(), null
        )

        if (cursor.moveToFirst()) {
            do {
                val category = cursor.getString(0)
                val value = cursor.getString(1)

                when (category) {
                    "type" -> type.add(value)
                    "generation" -> generation.add(value)
                    "region" -> region.add(value)
                    "habitat" -> habitat.add(value)
                    "color" -> color.add(value)
                    "shape" -> shape.add(value)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()

        Categories(
            type = type.sorted(),
            generation = generation.sorted(),
            region = region.sorted(),
            habitat = habitat.sorted(),
            color = color.sorted(),
            shape = shape.sorted()
        )
    }
}