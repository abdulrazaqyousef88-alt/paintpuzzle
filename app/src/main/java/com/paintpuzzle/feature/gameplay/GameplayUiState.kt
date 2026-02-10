package com.paintpuzzle.feature.gameplay

import com.paintpuzzle.core.model.GameBoard
import com.paintpuzzle.core.model.Skin

data class GameplayUiState(
    val board: GameBoard,
    val levelLabel: String,
    val gems: Int,
    val selectedSkin: Skin,
    val collisionFx: Boolean = false,
    val winFx: Boolean = false
)
