package com.paintpuzzle.core.engine

object ProgressCalculator {
    fun calculateProgress(paintedPaintable: Int, totalPaintable: Int): Int {
        if (totalPaintable <= 0) return 0
        return ((paintedPaintable.toFloat() / totalPaintable.toFloat()) * 100f)
            .coerceIn(0f, 100f)
            .toInt()
    }
}
