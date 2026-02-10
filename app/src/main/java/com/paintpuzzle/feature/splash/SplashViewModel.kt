package com.paintpuzzle.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paintpuzzle.core.data.PlayerPrefsRepository
import com.paintpuzzle.core.model.PlayerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(private val playerPrefsRepository: PlayerPrefsRepository) : ViewModel() {
    private val _profile = MutableStateFlow<PlayerProfile?>(null)
    val profile: StateFlow<PlayerProfile?> = _profile.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _profile.value = playerPrefsRepository.profileFlow.first()
        }
    }
}
