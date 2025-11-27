package com.cavies.pokedex.domain.model

data class Categories(
    val type: List<String> = emptyList(),
    val generation: List<String> = emptyList(),
    val region: List<String> = emptyList(),
    val habitat: List<String> = emptyList(),
    val color: List<String> = emptyList(),
    val shape: List<String> = emptyList()
)