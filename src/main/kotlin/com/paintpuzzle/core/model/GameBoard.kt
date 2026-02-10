package com.paintpuzzle.core.model

data class GameBoard(
    val levelId: Int,
    val rows: Int,
    val cols: Int,
    val sponge: GridPoint,
    val cells: List<List<CellType>>,
    val painted: BooleanArray,
    val totalPaintable: Int,
    val paintedPaintable: Int,
    val progressPercent: Int,
    val isWon: Boolean
) {
    fun indexOf(point: GridPoint): Int = point.row * cols + point.col
}
