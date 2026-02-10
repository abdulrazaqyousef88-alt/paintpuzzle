package com.paintpuzzle.core.model

data class PlayerProfile(
    val currentLevel: Int,
    val gems: Int,
    val ownedSkinIds: Set<String>,
    val selectedSkinId: String
)
