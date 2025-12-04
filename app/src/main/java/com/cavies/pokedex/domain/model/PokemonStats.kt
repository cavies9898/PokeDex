package com.cavies.pokedex.domain.model

data class PokemonStats(
    val hp: Int = 0,
    val attack: Int = 0,
    val defense: Int = 0,
    val specialAttack: Int = 0,
    val specialDefense: Int = 0,
    val speed: Int = 0,
    val total: Int = 0
) {
    fun toList() = listOf(hp, attack, defense, specialAttack, specialDefense, speed)
}