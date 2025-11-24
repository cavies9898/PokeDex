package com.cavies.pokedex.domain.model

data class Pokemon(
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String? = "",
    val stats: PokemonStats = PokemonStats(),
    val types: List<String> = emptyList(),
    val generation: String = "",
    val region: String = "",
    val habitat: String? = "",
    val color: String = "",
    val shape: String = "",
    val isFavorite: Boolean = false
)
