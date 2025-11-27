package com.cavies.pokedex.presentation.ui.components.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.presentation.ui.components.colors.PokemonTypeColor

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClickItem: (pokemon: Pokemon) -> Unit,
    onClickFavorite: (pokemon: Pokemon) -> Unit
) {

    val context = LocalContext.current
    val backgroundColor = PokemonTypeColor.get(pokemon.types.firstOrNull())
    val strongerColor = backgroundColor.copy(
        red = (backgroundColor.red * 0.9f),
        green = (backgroundColor.green * 0.9f),
        blue = (backgroundColor.blue * 0.9f)
    )

    ElevatedCard(
        onClick = { onClickItem(pokemon) },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = strongerColor
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 60.dp, y = (-60).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .blur(40.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(42.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.35f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { onClickFavorite(pokemon) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (pokemon.isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (pokemon.isFavorite) Color.Red else Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = "#${pokemon.id}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(pokemon.imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = pokemon.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = pokemon.name,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = Ellipsis,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    pokemon.types.forEach { type ->
                        val typeColor = PokemonTypeColor.get(type)

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = typeColor
                            )
                        ) {
                            Text(
                                text = type,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }

//                    Spacer(modifier = Modifier.weight(1f))

//                    IconButton(
//                        onClick = { onClickFavorite(pokemon) },
//                        modifier = Modifier.size(28.dp)
//                    ) {
//                        Icon(
//                            imageVector = if (pokemon.isFavorite)
//                                Icons.Filled.CheckCircle
//                            else
//                                Icons.Filled.CheckCircle,
//                            contentDescription = "Check",
//                            tint = if (pokemon.isFavorite) Color.Green else Color.White
//                        )
//                    }
                }
            }
        }
    }
}