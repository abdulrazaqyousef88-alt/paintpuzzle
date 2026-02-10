package com.paintpuzzle.navigation

object Routes {
    const val Splash = "splash"
    const val Gameplay = "gameplay"
    const val Win = "win/{reward}"
    const val Shop = "shop"

    fun win(reward: Int) = "win/$reward"
}
