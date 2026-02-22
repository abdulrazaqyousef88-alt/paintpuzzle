package com.paintpuzzle.core.engine

import com.paintpuzzle.core.model.CellType
import com.paintpuzzle.core.model.Direction
import com.paintpuzzle.core.model.GridPoint
import com.paintpuzzle.core.model.LevelDefinition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GameEngineTest {

    private val engine = GameEngine()

    @Test
    fun `swipe slides until border obstacle and paints traversed cells`() {
        val cells = listOf(
            listOf(CellType.BORDER, CellType.BORDER, CellType.BORDER, CellType.BORDER, CellType.BORDER),
            listOf(CellType.BORDER, CellType.PAINTABLE_WHITE, CellType.PAINTABLE_WHITE, CellType.WINDOW, CellType.BORDER),
            listOf(CellType.BORDER, CellType.PAINTABLE_WHITE, CellType.PAINTABLE_WHITE, CellType.PAINTABLE_WHITE, CellType.BORDER),
            listOf(CellType.BORDER, CellType.BORDER, CellType.BORDER, CellType.BORDER, CellType.BORDER)
        )
        val level = LevelDefinition(1, 4, 5, GridPoint(1, 1), cells, rewardGems = 25)
        val board = BoardFactory.fromLevel(level)

        val result = engine.applySwipe(board, Direction.RIGHT)
        val updated = assertIs<EngineResult.Updated>(result).board

        assertEquals(GridPoint(1, 2), updated.sponge)
        assertEquals(2, updated.paintedPaintable)
        assertEquals(40, updated.progressPercent)
    }

    @Test
    fun `immediate collision returns no-op`() {
        val cells = listOf(
            listOf(CellType.BORDER, CellType.BORDER, CellType.BORDER),
            listOf(CellType.BORDER, CellType.PAINTABLE_WHITE, CellType.BORDER),
            listOf(CellType.BORDER, CellType.BORDER, CellType.BORDER)
        )
        val level = LevelDefinition(1, 3, 3, GridPoint(1, 1), cells, rewardGems = 25)
        val board = BoardFactory.fromLevel(level)

        val result = engine.applySwipe(board, Direction.UP)
        val noOp = assertIs<EngineResult.NoOp>(result)

        assertTrue(noOp.collision)
        assertEquals(GridPoint(1, 1), noOp.board.sponge)
    }
}
