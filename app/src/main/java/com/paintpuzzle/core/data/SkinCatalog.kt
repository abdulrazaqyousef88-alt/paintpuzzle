package com.paintpuzzle.core.data

import com.paintpuzzle.core.model.Skin

object SkinCatalog {
    val skins: List<Skin> = listOf(
        Skin("classic", "Classic", 0, "#4FC3F7", "sponge_classic"),
        Skin("sunset", "Sunset", 120, "#FF7043", "sponge_sunset"),
        Skin("mint", "Mint", 180, "#66BB6A", "sponge_mint"),
        Skin("neon", "Neon", 260, "#D500F9", "sponge_neon"),
        Skin("galaxy", "Galaxy", 350, "#5C6BC0", "sponge_galaxy")
    )
}
