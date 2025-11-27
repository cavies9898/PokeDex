package com.cavies.pokedex.presentation.ui.home.common.filter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.ShapeLine
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.ui.graphics.vector.ImageVector
import com.cavies.pokedex.domain.model.Pokemon

enum class FilterItem(
    val displayName: String,
    val icon: ImageVector,
    val selector: ((Pokemon) -> Set<String>)? = null,
    val directFilter: ((List<Pokemon>) -> List<Pokemon>)? = null,
    val hasOptions: Boolean
) {
    All(
        "All",
        Icons.Outlined.FormatListNumbered,
        selector = null,
        directFilter = { pokemons -> pokemons },
        hasOptions = false
    ),

    Type(
        "Type",
        Icons.Outlined.Category,
        selector = { p -> p.types.toSet() },
        hasOptions = true
    ),

    Generation(
        "Generation",
        Icons.Outlined.AutoAwesome,
        selector = { p -> setOf(p.generation) },
        hasOptions = true
    ),

    Region(
        "Region",
        Icons.Outlined.Public,
        selector = { p -> setOf(p.region) },
        hasOptions = true
    ),

    Habitat(
        "Habitat",
        Icons.Outlined.Park,
        selector = { p -> p.habitat?.let { setOf(it) } ?: emptySet() },
        hasOptions = true
    ),

    Color(
        "Color",
        Icons.Outlined.Palette,
        selector = { p -> setOf(p.color) },
        hasOptions = true
    ),

    Shape(
        "Shape",
        Icons.Outlined.ShapeLine,
        selector = { p -> setOf(p.shape) },
        hasOptions = true
    ),

    // DIRECT FILTERS (no options)
    Alphabet(
        "Alphabet",
        Icons.Outlined.SortByAlpha,
        directFilter = { it.sortedBy { p -> p.name.lowercase() } },
        hasOptions = false
    ),

    Total(
        "Total",
        Icons.Outlined.FormatListNumbered,
        directFilter = { it.sortedByDescending { p -> p.stats.total } },
        hasOptions = false
    ),

    HP(
        "HP",
        Icons.Filled.Favorite,
        directFilter = { it.sortedByDescending { p -> p.stats.hp } },
        hasOptions = false
    ),

    Attack(
        "Attack",
        Icons.Outlined.FlashOn,
        directFilter = { it.sortedByDescending { p -> p.stats.attack } },
        hasOptions = false
    ),

    Defense(
        "Defense",
        Icons.Outlined.Shield,
        directFilter = { it.sortedByDescending { p -> p.stats.defense } },
        hasOptions = false
    ),

    SpecialAttack(
        "Spec. Atk",
        Icons.Outlined.Bolt,
        directFilter = { it.sortedByDescending { p -> p.stats.specialAttack } },
        hasOptions = false
    ),

    SpecialDefense(
        "Spec. Def",
        Icons.Outlined.Security,
        directFilter = { it.sortedByDescending { p -> p.stats.specialDefense } },
        hasOptions = false
    ),

    Speed(
        "Speed",
        Icons.Outlined.Speed,
        directFilter = { it.sortedByDescending { p -> p.stats.speed } },
        hasOptions = false
    ),

    Id(
        "Number #",
        Icons.Outlined.Numbers,
        directFilter = { it.sortedBy { p -> p.id } },
        hasOptions = false
    );
}




