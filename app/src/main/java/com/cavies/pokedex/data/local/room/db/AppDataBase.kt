package com.cavies.pokedex.data.local.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cavies.pokedex.data.local.room.dao.GameDao
import com.cavies.pokedex.data.local.room.dao.PokemonDao
import com.cavies.pokedex.data.local.room.entity.Game
import com.cavies.pokedex.data.local.room.entity.Pokemon
import com.cavies.pokedex.data.local.room.entity.PokemonGameCrossRef

@Database(
    entities = [Pokemon::class, Game::class, PokemonGameCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pokedex.db"
                )
                    .createFromAsset("databases/pokedex.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
