package com.paintpuzzle.feature.gameplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paintpuzzle.core.data.LevelRepository
import com.paintpuzzle.core.data.PlayerPrefsRepository
import com.paintpuzzle.core.data.SkinCatalog
import com.paintpuzzle.core.engine.BoardFactory
import com.paintpuzzle.core.engine.EngineResult
import com.paintpuzzle.core.engine.GameEngine
import com.paintpuzzle.core.model.Direction
import com.paintpuzzle.core.model.PlayerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameplayViewModel(
    private val playerPrefsRepository: PlayerPrefsRepository,
    private val levelRepository: LevelRepository
) : ViewModel() {

    private val engine = GameEngine()

    private val _state = MutableStateFlow<GameplayScreenState?>(null)
    val state: StateFlow<GameplayScreenState?> = _state.asStateFlow()

    fun bootstrap(profile: PlayerProfile) {
        val level = levelRepository.getLevel(profile.currentLevel)
        val board = BoardFactory.fromLevel(level)
        _state.value = GameplayScreenState(
            ui = GameplayUiState(
                board = board,
                levelLabel = "Level ${profile.currentLevel}",
                gems = profile.gems,
                selectedSkin = SkinCatalog.skins.firstOrNull { it.id == profile.selectedSkinId } ?: SkinCatalog.skins.first()
            ),
            profile = profile,
            rewardGems = level.rewardGems,
            levelIndex = profile.currentLevel
        )
    }

    fun onSwipe(direction: Direction, onWin: (Int) -> Unit) {
        val current = _state.value ?: return
        when (val result = engine.applySwipe(current.ui.board, direction)) {
            is EngineResult.NoOp -> {
                _state.value = current.copy(ui = current.ui.copy(collisionFx = result.collision))
            }
            is EngineResult.Updated -> {
                val ui = current.ui.copy(
                    board = result.board,
                    collisionFx = true,
                    winFx = result.wonThisMove
                )
                _state.value = current.copy(ui = ui)

                if (result.wonThisMove) {
                    val updatedProfile = current.profile.copy(gems = current.profile.gems + current.rewardGems)
                    _state.value = _state.value?.copy(profile = updatedProfile, ui = ui.copy(gems = updatedProfile.gems))
                    viewModelScope.launch { playerPrefsRepository.saveProfile(updatedProfile) }
                    onWin(current.rewardGems)
                }
            }
        }
    }

    fun clearFx() {
        val current = _state.value ?: return
        _state.value = current.copy(ui = current.ui.copy(collisionFx = false))
    }

    data class GameplayScreenState(
        val ui: GameplayUiState,
        val profile: PlayerProfile,
        val rewardGems: Int,
        val levelIndex: Int
    )
}
