package com.paintpuzzle.core.data

import com.paintpuzzle.core.model.CellType
import com.paintpuzzle.core.model.GridPoint
import com.paintpuzzle.core.model.LevelDefinition

class LevelRepository {
    fun getLevel(id: Int): LevelDefinition {
        val normalized = ((id - 1) % 20) + 1
        val base = baseLevel(normalized)
        return base.copy(id = normalized, rewardGems = 25 + normalized * 3)
    }

    private fun baseLevel(level: Int): LevelDefinition {
        val rows = 9
        val cols = 9
        val grid = MutableList(rows) { r ->
            MutableList(cols) { c ->
                if (r == 0 || c == 0 || r == rows - 1 || c == cols - 1) CellType.BORDER else CellType.PAINTABLE_WHITE
            }
        }

        val obstacleCol = 2 + (level % 5)
        for (r in 2..6) {
            if (r != 4) grid[r][obstacleCol] = when (r % 5) {
                0 -> CellType.WINDOW
                1 -> CellType.DOOR
                2 -> CellType.VENT
                3 -> CellType.BUSH
                else -> CellType.WOOD_BEAM
            }
        }

        return LevelDefinition(
            id = level,
            rows = rows,
            cols = cols,
            start = GridPoint(1, 1),
            cells = grid,
            rewardGems = 0
        )
    }
}
