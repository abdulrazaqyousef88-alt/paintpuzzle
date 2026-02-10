package com.paintpuzzle.feature.shop

import com.paintpuzzle.core.model.PlayerProfile
import com.paintpuzzle.core.model.Skin

sealed class PurchaseResult {
    data class Success(val profile: PlayerProfile) : PurchaseResult()
    data object AlreadyOwned : PurchaseResult()
    data object InsufficientGems : PurchaseResult()
}

class PurchaseSkinUseCase {
    fun execute(profile: PlayerProfile, skin: Skin): PurchaseResult {
        if (skin.id in profile.ownedSkinIds) return PurchaseResult.AlreadyOwned
        if (profile.gems < skin.costGems) return PurchaseResult.InsufficientGems

        val next = profile.copy(
            gems = profile.gems - skin.costGems,
            ownedSkinIds = profile.ownedSkinIds + skin.id,
            selectedSkinId = skin.id
        )
        return PurchaseResult.Success(next)
    }
}
