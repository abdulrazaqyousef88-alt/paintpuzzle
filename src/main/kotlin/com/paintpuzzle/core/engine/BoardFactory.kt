package com.paintpuzzle.core.engine

import com.paintpuzzle.core.model.CellType
import com.paintpuzzle.core.model.GameBoard
import com.paintpuzzle.core.model.LevelDefinition

object BoardFactory {
    fun fromLevel(level: LevelDefinition): GameBoard {
        val totalPaintable = level.cells.sumOf { row -> row.count { it == CellType.PAINTABLE_WHITE } }
        val painted = BooleanArray(level.rows * level.cols)
        var paintedCount = 0

        val startCell = level.cells[level.start.row][level.start.col]
        if (startCell == CellType.PAINTABLE_WHITE) {
            painted[level.start.row * level.cols + level.start.col] = true
            paintedCount = 1
        }

        return GameBoard(
            levelId = level.id,
            rows = level.rows,
            cols = level.cols,
            sponge = level.start,
            cells = level.cells,
            painted = painted,
            totalPaintable = totalPaintable,
            paintedPaintable = paintedCount,
            progressPercent = ProgressCalculator.calculateProgress(paintedCount, totalPaintable),
            isWon = paintedCount == totalPaintable && totalPaintable > 0
        )
    }
}
