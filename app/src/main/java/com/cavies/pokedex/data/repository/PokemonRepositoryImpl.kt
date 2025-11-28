package com.cavies.pokedex.data.repository

import android.database.sqlite.SQLiteDatabase
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.model.PokemonStats
import com.cavies.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val sqliteDb: SQLiteDatabase,
) : PokemonRepository {

    override suspend fun getPokemons(): List<Pokemon> = withContext(Dispatchers.IO) {

        val result = mutableMapOf<Int, Pokemon>()
        val statsTemp = mutableMapOf<Int, MutableMap<String, Int>>()

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
                val baseName = cursor.getString(1)
                val identifier = cursor.getString(2)
                val type = cursor.getString(3)
                val generation = cursor.getString(4)
                val region = cursor.getString(5)
                val habitat = cursor.getString(6)
                val color = cursor.getString(7)
                val shape = cursor.getString(8)
                val statName = cursor.getString(9)
                val statValue = cursor.getInt(10)

                val displayName = if (identifier.contains("-")) {
                    identifier.split("-")
                        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
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


    override suspend fun getPokemonById(id: Int): Pokemon? = withContext(Dispatchers.IO) {

        val cursor = sqliteDb.rawQuery(
            """
        SELECT 
            p.id,
            psn.name AS pokemon_name,
            p.identifier AS identifier,
            g.identifier AS generation,
            rn.name AS region,
            h.identifier AS habitat,
            c.identifier AS color,
            s.identifier AS shape
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
        WHERE p.id = ?
        LIMIT 1;
        """.trimIndent(),
            arrayOf(id.toString())
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            return@withContext null
        }

        val baseName = cursor.getString(1)
        val identifier = cursor.getString(2)
        val generation = cursor.getString(3)
        val region = cursor.getString(4)
        val habitat = cursor.getString(5)
        val color = cursor.getString(6)
        val shape = cursor.getString(7)

        cursor.close()

        val displayName =
            if (identifier.contains("-"))
                identifier.split("-")
                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
            else
                baseName

        val types = getPokemonTypes(id)
        val stats = getPokemonStats(id)

        Pokemon(
            id = id,
            name = displayName,
            imageUrl = buildImageUrl(id),
            generation = generation,
            region = region,
            habitat = habitat,
            color = color,
            shape = shape,
            types = types,
            stats = stats
        )
    }

    private fun getPokemonTypes(id: Int): List<String> {
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

        val list = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }

    private fun getPokemonStats(id: Int): PokemonStats {
        val cursor = sqliteDb.rawQuery(
            """
        SELECT st.identifier, ps.base_stat
        FROM pokemon_stats ps
        JOIN stats st ON st.id = ps.stat_id
        WHERE ps.pokemon_id = ?
        """.trimIndent(),
            arrayOf(id.toString())
        )

        val stats = mutableMapOf<String, Int>()

        if (cursor.moveToFirst()) {
            do {
                stats[cursor.getString(0)] = cursor.getInt(1)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return PokemonStats(
            hp = stats["hp"] ?: 0,
            attack = stats["attack"] ?: 0,
            defense = stats["defense"] ?: 0,
            specialAttack = stats["special-attack"] ?: 0,
            specialDefense = stats["special-defense"] ?: 0,
            speed = stats["speed"] ?: 0,
            total = stats.values.sum()
        )
    }


    private fun buildImageUrl(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

}


