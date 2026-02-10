package com.paintpuzzle.feature.win

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paintpuzzle.core.data.PlayerPrefsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WinViewModel(private val repo: PlayerPrefsRepository) : ViewModel() {
    fun nextLevel(onDone: () -> Unit) {
        viewModelScope.launch {
            val p = repo.profileFlow.first()
            val next = if (p.currentLevel >= 20) 1 else p.currentLevel + 1
            repo.saveProfile(p.copy(currentLevel = next))
            onDone()
        }
    }
}
