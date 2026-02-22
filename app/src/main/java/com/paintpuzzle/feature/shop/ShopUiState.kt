package com.paintpuzzle.feature.shop

import com.paintpuzzle.core.model.Skin

enum class SkinStatus { LOCKED, OWNED, SELECTED }

data class ShopSkinItem(
    val skin: Skin,
    val status: SkinStatus
)

data class ShopUiState(
    val gems: Int,
    val skins: List<ShopSkinItem>
)
