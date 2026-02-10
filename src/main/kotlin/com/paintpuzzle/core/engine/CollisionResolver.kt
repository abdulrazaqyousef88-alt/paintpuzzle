package com.paintpuzzle.core.engine

import com.paintpuzzle.core.model.CellType
import com.paintpuzzle.core.model.GameBoard
import com.paintpuzzle.core.model.GridPoint

object CollisionResolver {
    fun isBlocked(board: GameBoard, point: GridPoint): Boolean {
        if (point.row !in 0 until board.rows || point.col !in 0 until board.cols) return true
        return when (board.cells[point.row][point.col]) {
            CellType.BORDER,
            CellType.WINDOW,
            CellType.DOOR,
            CellType.VENT,
            CellType.BUSH,
            CellType.WOOD_BEAM,
            CellType.EMPTY_BACKGROUND -> true
            CellType.PAINTABLE_WHITE -> false
        }
    }
}
