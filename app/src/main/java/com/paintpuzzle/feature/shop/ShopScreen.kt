package com.paintpuzzle.feature.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShopScreen(state: ShopUiState, onClickSkin: (String) -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Shop")
            Text("💎 ${state.gems}")
        }
        Button(onClick = onBack, modifier = Modifier.padding(vertical = 8.dp)) { Text("Back") }

        LazyColumn {
            items(state.skins) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(item.skin.displayName)
                        val label = when (item.status) {
                            SkinStatus.LOCKED -> "Buy ${item.skin.costGems}"
                            SkinStatus.OWNED -> "Select"
                            SkinStatus.SELECTED -> "Selected"
                        }
                        Button(onClick = { onClickSkin(item.skin.id) }, enabled = item.status != SkinStatus.SELECTED) {
                            Text(label)
                        }
                    }
                }
            }
        }
    }
}
