package com.cavies.pokedex.data.repository

import android.database.sqlite.SQLiteDatabase
import com.cavies.pokedex.data.local.room.dao.FavoritesDao
import com.cavies.pokedex.data.local.room.entity.FavoritesEntity
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.util.Log
import com.cavies.pokedex.domain.model.PokemonStats
import com.cavies.pokedex.presentation.ui.home.Categories
import kotlin.system.measureTimeMillis

class PokemonRepositoryImpl @Inject constructor(
    private val sqliteDb: SQLiteDatabase,
    private val favoritesDao: FavoritesDao,
) : PokemonRepository {

    override suspend fun getPokemons(): List<Pokemon> = withContext(Dispatchers.IO) {
        trackTime("getPokemonsWithStats") {

            val result = mutableMapOf<Int, Pokemon>()
            val statsTemp = mutableMapOf<Int, MutableMap<String, Int>>() // acumulador de stats

            val cursor = sqliteDb.rawQuery(
                """
            SELECT DISTINCT
                p.id,
                psn.name AS pokemon_name,
                p.identifier AS identifier,
                t.identifier AS type,
                
                g.identifier AS generation,
                rn.name AS region,
            
                h.identifier AS habitat,
                c.identifier AS color,
                s.identifier AS shape,
            
                st.identifier AS stat_name,
                ps.base_stat AS stat_value,
            
                pt.slot AS type_slot
            
            FROM pokemon p
            LEFT JOIN pokemon_species ps2 ON ps2.id = p.species_id
            LEFT JOIN pokemon_species_names psn 
                   ON psn.pokemon_species_id = ps2.id
                   AND psn.local_language_id = 9
        
            LEFT JOIN generations g ON g.id = ps2.generation_id
            LEFT JOIN region_names rn 
                   ON rn.region_id = g.main_region_id 
                   AND rn.local_language_id = 9
        
            LEFT JOIN pokemon_habitats h ON h.id = ps2.habitat_id
            LEFT JOIN pokemon_colors c ON c.id = ps2.color_id
            LEFT JOIN pokemon_shapes s ON s.id = ps2.shape_id
        
            LEFT JOIN pokemon_types pt ON pt.pokemon_id = p.id
            LEFT JOIN types t ON t.id = pt.type_id
        
            LEFT JOIN pokemon_stats ps ON ps.pokemon_id = p.id
            LEFT JOIN stats st ON st.id = ps.stat_id
        
            ORDER BY p.id, pt.slot;
            """.trimIndent(),
                null
            )

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(0)
                    val baseName = cursor.getString(1)           // nombre bonito
                    val identifier = cursor.getString(2)         // para variantes
                    val type = cursor.getString(3)
                    val generation = cursor.getString(4)
                    val region = cursor.getString(5)
                    val habitat = cursor.getString(6)
                    val color = cursor.getString(7)
                    val shape = cursor.getString(8)
                    val statName = cursor.getString(9)
                    val statValue = cursor.getInt(10)

                    val displayName = if (identifier.contains("-")) {
                        identifier.split("-").joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
                    } else {
                        baseName
                    }

                    val pokemon = result.getOrPut(id) {
                        Pokemon(
                            id = id,
                            name = displayName,
                            imageUrl = buildImageUrl(id),
                            generation = generation,
                            region = region,
                            habitat = habitat,
                            color = color,
                            shape = shape,
                            types = mutableListOf()
                        )
                    }

                    type?.let { t ->
                        val list = pokemon.types as MutableList
                        if (!list.contains(t)) list.add(t)
                    }

                    statName?.let { name ->
                        val map = statsTemp.getOrPut(id) { mutableMapOf() }
                        map[name] = statValue
                    }

                } while (cursor.moveToNext())
            }

            cursor.close()

            result.forEach { (id, pokemon) ->
                val stats = statsTemp[id] ?: emptyMap()
                val statObj = PokemonStats(
                    hp = stats["hp"] ?: 0,
                    attack = stats["attack"] ?: 0,
                    defense = stats["defense"] ?: 0,
                    specialAttack = stats["special-attack"] ?: 0,
                    specialDefense = stats["special-defense"] ?: 0,
                    speed = stats["speed"] ?: 0,
                    total = stats.values.sum()
                )
                result[id] = pokemon.copy(stats = statObj)
            }

            result.values.toList()
        }
    }

    override suspend fun getCategories(): Categories = withContext(Dispatchers.IO) {
        trackTime("getFavoriteIds") {

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


    private fun buildImageUrl(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"


    override suspend fun getFavoriteIds(): List<Int> =
        trackTime("getFavoriteIds") {
            favoritesDao.getAllFavorites().map { it.id }
        }

    override suspend fun addFavorite(id: Int, name: String) =
        trackTime("addFavorite($id)") {
            favoritesDao.insertFavorite(FavoritesEntity(id, name))
        }

    override suspend fun removeFavorite(id: Int) =
        trackTime("removeFavorite($id)") {
            favoritesDao.deleteFavorite(id)
        }
}


inline fun <T> trackTime(tag: String, block: () -> T): T {
    var result: T
    val time = measureTimeMillis {
        result = block()
    }
    Log.d("PERF", "$tag → ${time}ms")
    return result
}


