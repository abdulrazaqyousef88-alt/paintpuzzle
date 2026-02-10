package com.paintpuzzle.feature.gameplay

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.paintpuzzle.core.model.CellType
import com.paintpuzzle.core.model.Direction
import com.paintpuzzle.core.model.GridPoint

@Composable
fun GameplayScreen(
    state: GameplayUiState,
    onSwipe: (Direction) -> Unit,
    onShop: () -> Unit,
    onClearFx: () -> Unit
) {
    if (state.collisionFx) {
        LaunchedEffect(state.collisionFx) { onClearFx() }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize(0.08f)) {
            Text(state.levelLabel)
            Text("💎 ${state.gems}")
            Button(onClick = onShop) { Text("Shop") }
        }

        LinearProgressIndicator(
            progress = { state.board.progressPercent / 100f },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text("${state.board.progressPercent}%")

        val points = buildList {
            for (r in 0 until state.board.rows) {
                for (c in 0 until state.board.cols) add(GridPoint(r, c))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {},
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val dir = dragToDirection(dragAmount)
                            onSwipe(dir)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(state.board.cols)) {
                items(points) { point ->
                    val cellType = state.board.cells[point.row][point.col]
                    val painted = state.board.painted[state.board.indexOf(point)]
                    val isSponge = point == state.board.sponge
                    val color = when {
                        isSponge -> Color(0xFFFFA726)
                        cellType != CellType.PAINTABLE_WHITE -> Color(0xFF546E7A)
                        painted -> Color(android.graphics.Color.parseColor(state.selectedSkin.trailColorHex))
                        else -> Color.White
                    }
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .padding(1.dp)
                            .background(color)
                    )
                }
            }
        }
    }
}

private fun dragToDirection(dragAmount: Offset): Direction {
    return if (kotlin.math.abs(dragAmount.x) > kotlin.math.abs(dragAmount.y)) {
        if (dragAmount.x >= 0) Direction.RIGHT else Direction.LEFT
    } else {
        if (dragAmount.y >= 0) Direction.DOWN else Direction.UP
    }
}
