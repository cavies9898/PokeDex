package com.cavies.pokedex.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cavies.pokedex.data.local.room.dao.FavoritesDao
import com.cavies.pokedex.data.local.room.db.AppDatabase
import com.cavies.pokedex.data.local.room.db.DatabaseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import java.util.concurrent.Executors

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * SQLITE
     * */

    @Provides
    @Singleton
    fun provideDatabaseHelper(
        @ApplicationContext context: Context
    ): DatabaseHelper {
        return DatabaseHelper(context)
    }

    @Provides
    @Singleton
    fun provideSQLiteDatabase(
        dbHelper: DatabaseHelper
    ): SQLiteDatabase {
        return dbHelper.readableDatabase
    }

    /**
     * ROOM
     * */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pokedex.db"
        )
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .fallbackToDestructiveMigration()
            .addMigrations( /* add migrations latter */ )
            .setQueryCallback({ sqlQuery, _ ->
                Log.d("SQL_DEBUG", sqlQuery)
            }, Executors.newSingleThreadExecutor())
            .build()
    }

    @Provides
    fun provideFavoritesDao(db: AppDatabase): FavoritesDao = db.favoritesDao()
}