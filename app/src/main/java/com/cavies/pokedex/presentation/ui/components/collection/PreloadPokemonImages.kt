package com.cavies.pokedex.presentation.ui.components.collection

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cavies.pokedex.domain.model.Pokemon
import kotlinx.coroutines.delay

suspend fun preloadPokemonImages(
    context: Context,
    pokemonList: List<Pokemon>
) {
    val loader = ImageLoader(context)

    pokemonList.chunked(50).forEach { batch ->
        batch.forEach { p ->
            val request = ImageRequest.Builder(context)
                .data(p.imageUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()

            loader.enqueue(request)
        }
        delay(500)
    }
}
