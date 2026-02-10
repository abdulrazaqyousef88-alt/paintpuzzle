package com.paintpuzzle.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.paintpuzzle.core.model.PlayerProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "player_prefs")

class PlayerPrefsRepository(private val context: Context) {
    private object Keys {
        val CurrentLevel = intPreferencesKey("current_level")
        val Gems = intPreferencesKey("gems")
        val OwnedSkins = stringPreferencesKey("owned_skins_csv")
        val SelectedSkin = stringPreferencesKey("selected_skin")
    }

    val profileFlow: Flow<PlayerProfile> = context.dataStore.data.map { prefs ->
        val owned = prefs[Keys.OwnedSkins]?.split(',')?.filter { it.isNotBlank() }?.toSet() ?: setOf("classic")
        PlayerProfile(
            currentLevel = prefs[Keys.CurrentLevel] ?: 1,
            gems = prefs[Keys.Gems] ?: 0,
            ownedSkinIds = owned,
            selectedSkinId = prefs[Keys.SelectedSkin] ?: "classic"
        )
    }

    suspend fun saveProfile(profile: PlayerProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CurrentLevel] = profile.currentLevel
            prefs[Keys.Gems] = profile.gems
            prefs[Keys.OwnedSkins] = profile.ownedSkinIds.joinToString(",")
            prefs[Keys.SelectedSkin] = profile.selectedSkinId
        }
    }
}
