package com.paintpuzzle.core.engine

import com.paintpuzzle.core.model.CellType
import com.paintpuzzle.core.model.Direction
import com.paintpuzzle.core.model.GameBoard
import com.paintpuzzle.core.model.GridPoint

class GameEngine {

    fun applySwipe(board: GameBoard, direction: Direction): EngineResult {
        if (board.isWon) return EngineResult.NoOp(board)

        val path = computeSlidePath(board, board.sponge, direction)
        if (path.size <= 1) return EngineResult.NoOp(board, collision = true)

        val newPainted = board.painted.copyOf()
        var paintedCount = board.paintedPaintable

        for (point in path) {
            if (isPaintable(board, point)) {
                val idx = board.indexOf(point)
                if (!newPainted[idx]) {
                    newPainted[idx] = true
                    paintedCount++
                }
            }
        }

        val progress = ProgressCalculator.calculateProgress(paintedCount, board.totalPaintable)
        val isWon = paintedCount == board.totalPaintable
        val updated = board.copy(
            sponge = path.last(),
            painted = newPainted,
            paintedPaintable = paintedCount,
            progressPercent = progress,
            isWon = isWon
        )

        return EngineResult.Updated(
            board = updated,
            traversedPath = path,
            collisionAt = nextPoint(path.last(), direction),
            wonThisMove = isWon
        )
    }

    private fun computeSlidePath(board: GameBoard, start: GridPoint, direction: Direction): List<GridPoint> {
        val path = mutableListOf(start)
        var current = start

        while (true) {
            val next = nextPoint(current, direction)
            if (CollisionResolver.isBlocked(board, next)) break
            path += next
            current = next
        }
        return path
    }

    private fun isPaintable(board: GameBoard, point: GridPoint): Boolean {
        return board.cells[point.row][point.col] == CellType.PAINTABLE_WHITE
    }

    private fun nextPoint(point: GridPoint, direction: Direction): GridPoint {
        return when (direction) {
            Direction.UP -> GridPoint(point.row - 1, point.col)
            Direction.DOWN -> GridPoint(point.row + 1, point.col)
            Direction.LEFT -> GridPoint(point.row, point.col - 1)
            Direction.RIGHT -> GridPoint(point.row, point.col + 1)
        }
    }
}
