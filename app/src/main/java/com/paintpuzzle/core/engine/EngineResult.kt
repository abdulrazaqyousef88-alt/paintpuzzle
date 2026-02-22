package com.paintpuzzle.core.engine

import com.paintpuzzle.core.model.GameBoard
import com.paintpuzzle.core.model.GridPoint

sealed class EngineResult {
    data class Updated(
        val board: GameBoard,
        val traversedPath: List<GridPoint>,
        val collisionAt: GridPoint,
        val wonThisMove: Boolean
    ) : EngineResult()

    data class NoOp(
        val board: GameBoard,
        val collision: Boolean = false
    ) : EngineResult()
}
