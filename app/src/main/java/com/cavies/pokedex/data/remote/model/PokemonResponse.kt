package com.cavies.pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**data class from basic response api/pokemon */
data class PokemonListResponse(
    val results: List<PokemonBasicDto>
)

data class PokemonBasicDto(
    val name: String,
    val url: String
)

/**data class from basic response api/pokemon/{id} */
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val types: List<PokemonTypeSlotDto>,
    @SerializedName("sprites") val sprites: PokemonSpritesDto,
)

data class PokemonTypeSlotDto(
    val type: PokemonTypeDto
)

data class PokemonTypeDto(
    val name: String,
    val url: String
)
data class PokemonSpritesDto(
    @SerializedName("front_default") val imageUrl: String?,
    @SerializedName("other") val others: PokemonDetailOtherResponse
)

data class PokemonDetailOtherResponse(
    @SerializedName("official-artwork") val officialArtwork: FrontDefaultResponse
)

data class FrontDefaultResponse(
    @SerializedName("front_default") val imageUrl: String?
)

/**Pokemon Type Response*/