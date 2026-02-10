package com.paintpuzzle.feature.shop

import com.paintpuzzle.core.model.PlayerProfile
import com.paintpuzzle.core.model.Skin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PurchaseSkinUseCaseTest {

    private val useCase = PurchaseSkinUseCase()
    private val skin = Skin("sunset", "Sunset", 120, "#FF7043", "sponge_sunset")

    @Test
    fun `successful purchase deducts gems owns skin and selects it`() {
        val profile = PlayerProfile(1, gems = 150, ownedSkinIds = setOf("classic"), selectedSkinId = "classic")
        val result = useCase.execute(profile, skin)

        val success = assertIs<PurchaseResult.Success>(result)
        assertEquals(30, success.profile.gems)
        assertEquals(setOf("classic", "sunset"), success.profile.ownedSkinIds)
        assertEquals("sunset", success.profile.selectedSkinId)
    }

    @Test
    fun `insufficient gems keeps state unchanged`() {
        val profile = PlayerProfile(1, gems = 50, ownedSkinIds = setOf("classic"), selectedSkinId = "classic")
        val result = useCase.execute(profile, skin)

        assertIs<PurchaseResult.InsufficientGems>(result)
    }
}
