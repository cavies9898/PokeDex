package com.cavies.pokedex.di

import com.cavies.pokedex.data.repository.CategoriesRepositoryImpl
import com.cavies.pokedex.data.repository.FavoriteRepositoryImpl
import com.cavies.pokedex.data.repository.PokemonRepositoryImpl
import com.cavies.pokedex.domain.repository.CategoriesRepository
import com.cavies.pokedex.domain.repository.FavoriteRepository
import com.cavies.pokedex.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPokemonRepository(
        impl: PokemonRepositoryImpl
    ): PokemonRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindCategoriesRepository(
        impl: CategoriesRepositoryImpl
    ): CategoriesRepository
}
