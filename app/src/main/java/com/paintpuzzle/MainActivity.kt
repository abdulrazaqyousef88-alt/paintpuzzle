package com.paintpuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.paintpuzzle.navigation.AppNavHost
import com.paintpuzzle.ui.theme.PaintPuzzleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaintPuzzleTheme {
                AppNavHost(applicationContext)
            }
        }
    }
}
