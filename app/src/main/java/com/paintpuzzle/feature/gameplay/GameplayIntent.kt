package com.paintpuzzle.feature.gameplay

import com.paintpuzzle.core.model.Direction

sealed class GameplayIntent {
    data class Swipe(val direction: Direction) : GameplayIntent()
    data object RetryLevel : GameplayIntent()
    data object NextLevel : GameplayIntent()
}
