package com.paintpuzzle.feature.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paintpuzzle.core.data.PlayerPrefsRepository
import com.paintpuzzle.core.data.SkinCatalog
import com.paintpuzzle.core.model.PlayerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShopViewModel(private val repo: PlayerPrefsRepository) : ViewModel() {
    private val purchaseSkinUseCase = PurchaseSkinUseCase()

    private val _state = MutableStateFlow<ShopUiState?>(null)
    val state: StateFlow<ShopUiState?> = _state.asStateFlow()

    private var profile: PlayerProfile? = null

    fun load() {
        viewModelScope.launch {
            val p = repo.profileFlow.first()
            profile = p
            _state.value = p.toShopState()
        }
    }

    fun buyOrSelect(skinId: String) {
        val p = profile ?: return
        val skin = SkinCatalog.skins.first { it.id == skinId }

        if (skin.id in p.ownedSkinIds) {
            val updated = p.copy(selectedSkinId = skin.id)
            persist(updated)
            return
        }

        when (val result = purchaseSkinUseCase.execute(p, skin)) {
            PurchaseResult.AlreadyOwned -> Unit
            PurchaseResult.InsufficientGems -> Unit
            is PurchaseResult.Success -> persist(result.profile)
        }
    }

    private fun persist(updated: PlayerProfile) {
        profile = updated
        _state.value = updated.toShopState()
        viewModelScope.launch { repo.saveProfile(updated) }
    }

    private fun PlayerProfile.toShopState(): ShopUiState {
        val items = SkinCatalog.skins.map { skin ->
            val status = when {
                selectedSkinId == skin.id -> SkinStatus.SELECTED
                ownedSkinIds.contains(skin.id) -> SkinStatus.OWNED
                else -> SkinStatus.LOCKED
            }
            ShopSkinItem(skin, status)
        }
        return ShopUiState(gems = gems, skins = items)
    }
}
