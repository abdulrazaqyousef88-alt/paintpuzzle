package com.paintpuzzle.core.engine

import kotlin.test.Test
import kotlin.test.assertEquals

class ProgressCalculatorTest {

    @Test
    fun `progress formula uses painted divided by total multiplied by 100`() {
        assertEquals(0, ProgressCalculator.calculateProgress(0, 10))
        assertEquals(25, ProgressCalculator.calculateProgress(1, 4))
        assertEquals(66, ProgressCalculator.calculateProgress(2, 3))
        assertEquals(100, ProgressCalculator.calculateProgress(10, 10))
    }
}
