package com.paintpuzzle.core.model

data class LevelDefinition(
    val id: Int,
    val rows: Int,
    val cols: Int,
    val start: GridPoint,
    val cells: List<List<CellType>>,
    val rewardGems: Int
)
