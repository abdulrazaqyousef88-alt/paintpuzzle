package com.paintpuzzle.feature.win

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun WinScreen(reward: Int, onNextLevel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("House Painted!", style = MaterialTheme.typography.headlineMedium)
        Text("100% Complete")
        Text("+${reward} Gems")
        Button(onClick = onNextLevel) { Text("Next Level") }
    }
}
