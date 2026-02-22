1) Executive Summary

### Self-Evaluation Gate (Yes/No)
| Validation Item | Yes/No | Notes |
|---|---|---|
| Full screen coverage | Yes | Splash/Main, Gameplay, Win, Shop all fully specified. |
| Collision logic completeness | Yes | Borders + static obstacles + no-move swipe handling included. |
| Progress formula correctness | Yes | `paintedPaintable / totalPaintable * 100` defined and tested. |
| Gems/shop consistency | Yes | Rewards, costs, ownership, selection and persistence aligned. |
| Edge-case coverage | Yes | Invalid levels, unreachable paint, repeated swipe, restore, precision handled. |

- Build a playable prototype for Android (minSdk 24) using **Kotlin + Jetpack Compose + MVVM**, with the game simulation isolated in a deterministic engine module.
- Core movement is **slide-until-collision** (not step movement): each swipe computes a straight path and stops one cell before obstacle/border.
- Painting is path-based: all traversed paintable cells become painted in one engine tick; progress uses `paintedPaintable / totalPaintable * 100` and win triggers exactly at 100%.
- Initial content targets **20 handcrafted levels**, each as a 2D grid with paintable walls and non-paintable blockers (windows, doors, vents, bushes, beams).
- UI screens implemented: **Splash/Main**, **Gameplay HUD + board**, **Win modal/screen with reward/confetti**, **Shop grid** with purchase/unlock/select skin flow.
- Persistent state handled via **Proto DataStore** (current level, gems, owned skins, selected skin) and optional Room-ready hooks for future analytics/history.
- Starter code blueprint includes engine interfaces, concrete movement algorithm, ViewModel contracts, repositories, level loader, and Compose screen scaffolds ready for immediate implementation.

2) Proposed Architecture

- **Presentation Layer (Compose + ViewModel)**
  - Renders immutable UI state only.
  - Dispatches player intents (`Swipe`, `Retry`, `NextLevel`, `BuySkin`, `SelectSkin`).
  - Observes `StateFlow<UiState>` from ViewModels.

- **Domain Layer (Game Engine + Use Cases)**
  - Pure Kotlin engine (no Android deps) for deterministic simulation.
  - Use cases:
    - `ProcessSwipeUseCase`
    - `LoadLevelUseCase`
    - `CalculateProgressUseCase`
    - `GrantWinRewardUseCase`
    - `PurchaseSkinUseCase`
  - Engine state and rendering state are separated:
    - Engine: grid, sponge position, painted mask, win state.
    - Rendering: colors, sprites, animation triggers.

- **Data Layer (Repositories + Local Storage)**
  - `LevelRepository` loads level definitions from JSON/assets.
  - `PlayerRepository` persists profile and economy data in Proto DataStore.
  - `SkinRepository` holds static skin catalog and purchase constraints.

- **Module split (single app module acceptable for prototype, with package boundaries):**
  - `core-engine` (pure kotlin package)
  - `feature-gameplay`
  - `feature-shop`
  - `feature-splash`
  - `core-data`
  - `core-ui`

- **Why Compose (run config enforced):**
  - Fast UI iteration for prototype, state-driven rendering aligns with MVVM + StateFlow, easier animation and HUD composition without XML boilerplate.

3) Project Structure

```text
app/
  src/main/java/com/paintpuzzle/
    App.kt
    MainActivity.kt
    navigation/
      AppNavHost.kt
      Routes.kt

    core/
      model/
        CellType.kt
        Direction.kt
        GridPoint.kt
        LevelDefinition.kt
        GameBoard.kt
        Skin.kt
        PlayerProfile.kt
      engine/
        GameEngine.kt
        EngineResult.kt
        CollisionResolver.kt
        ProgressCalculator.kt
      data/
        datastore/
          PlayerPreferencesSerializer.kt
          PlayerPreferences.proto
          PlayerLocalDataSource.kt
        repository/
          LevelRepositoryImpl.kt
          PlayerRepositoryImpl.kt
          SkinRepositoryImpl.kt
      ui/
        theme/
          Color.kt
          Type.kt
          Theme.kt
        components/
          ProgressHeader.kt
          GameCellComposable.kt
          SwipeLayer.kt

    feature/splash/
      SplashViewModel.kt
      SplashScreen.kt

    feature/gameplay/
      GameplayViewModel.kt
      GameplayUiState.kt
      GameplayIntent.kt
      GameplayScreen.kt

    feature/win/
      WinViewModel.kt
      WinScreen.kt

    feature/shop/
      ShopViewModel.kt
      ShopUiState.kt
      ShopScreen.kt

  src/main/assets/levels/
    level_001.json
    ...
    level_020.json

  src/test/java/com/paintpuzzle/
    core/engine/GameEngineTest.kt
    core/engine/ProgressCalculatorTest.kt
    feature/shop/PurchaseSkinUseCaseTest.kt

  src/androidTest/java/com/paintpuzzle/
    navigation/AppNavigationTest.kt
    feature/gameplay/GameplayScreenTest.kt
```

4) Kotlin Data Models

```kotlin
package com.paintpuzzle.core.model

enum class CellType {
    PAINTABLE_WHITE,    // counts toward totalPaintable
    PAINTED,            // runtime derived or persisted in board state
    BORDER,             // house frame
    WINDOW,
    DOOR,
    VENT,
    BUSH,
    WOOD_BEAM,
    EMPTY_BACKGROUND    // outside house visuals, non-playable
}

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class GridPoint(val row: Int, val col: Int)

data class LevelDefinition(
    val id: Int,
    val rows: Int,
    val cols: Int,
    val start: GridPoint,
    val cells: List<List<CellType>>, // immutable source map
    val rewardGems: Int
)

data class GameBoard(
    val levelId: Int,
    val rows: Int,
    val cols: Int,
    val sponge: GridPoint,
    val cells: List<List<CellType>>, // includes obstacle map + paintable base
    val painted: BooleanArray,       // size rows * cols; true if painted paintable cell
    val totalPaintable: Int,
    val paintedPaintable: Int,
    val progressPercent: Int,
    val isWon: Boolean
)

data class Skin(
    val id: String,
    val displayName: String,
    val costGems: Int,
    val trailColorHex: String,
    val spongeAsset: String
)

data class PlayerProfile(
    val currentLevel: Int,
    val gems: Int,
    val ownedSkinIds: Set<String>,
    val selectedSkinId: String
)
```

`painted` index helper:
```kotlin
fun GameBoard.indexOf(p: GridPoint): Int = p.row * cols + p.col
```

5) Core Engine Logic

```kotlin
package com.paintpuzzle.core.engine

import com.paintpuzzle.core.model.*

class GameEngine {

    fun applySwipe(board: GameBoard, direction: Direction): EngineResult {
        if (board.isWon) return EngineResult.NoOp(board)

        val path = computeSlidePath(board, board.sponge, direction)
        if (path.size <= 1) {
            // immediate collision, no movement; optional collision FX trigger
            return EngineResult.NoOp(board, collision = true)
        }

        val newPainted = board.painted.copyOf()
        var paintedCount = board.paintedPaintable

        // Paint every traversed cell including destination and origin if paintable
        for (p in path) {
            if (isPaintable(board, p)) {
                val idx = board.indexOf(p)
                if (!newPainted[idx]) {
                    newPainted[idx] = true
                    paintedCount++
                }
            }
        }

        val newSponge = path.last()
        val progress = calculateProgress(paintedCount, board.totalPaintable)
        val won = progress == 100

        val updated = board.copy(
            sponge = newSponge,
            painted = newPainted,
            paintedPaintable = paintedCount,
            progressPercent = progress,
            isWon = won
        )

        return EngineResult.Updated(
            board = updated,
            traversedPath = path,
            collisionAt = findCollisionPointAfter(path.last(), direction),
            collision = true,
            wonThisMove = won
        )
    }

    private fun computeSlidePath(board: GameBoard, start: GridPoint, direction: Direction): List<GridPoint> {
        val path = mutableListOf(start)
        var cur = start

        while (true) {
            val next = when (direction) {
                Direction.UP -> GridPoint(cur.row - 1, cur.col)
                Direction.DOWN -> GridPoint(cur.row + 1, cur.col)
                Direction.LEFT -> GridPoint(cur.row, cur.col - 1)
                Direction.RIGHT -> GridPoint(cur.row, cur.col + 1)
            }

            if (isBlocked(board, next)) break
            path += next
            cur = next
        }
        return path
    }

    private fun isBlocked(board: GameBoard, p: GridPoint): Boolean {
        if (p.row !in 0 until board.rows || p.col !in 0 until board.cols) return true
        return when (board.cells[p.row][p.col]) {
            CellType.BORDER,
            CellType.WINDOW,
            CellType.DOOR,
            CellType.VENT,
            CellType.BUSH,
            CellType.WOOD_BEAM,
            CellType.EMPTY_BACKGROUND -> true
            CellType.PAINTABLE_WHITE,
            CellType.PAINTED -> false
        }
    }

    private fun isPaintable(board: GameBoard, p: GridPoint): Boolean {
        return board.cells[p.row][p.col] == CellType.PAINTABLE_WHITE || board.cells[p.row][p.col] == CellType.PAINTED
    }

    private fun calculateProgress(paintedPaintable: Int, totalPaintable: Int): Int {
        if (totalPaintable <= 0) return 0
        return ((paintedPaintable.toFloat() / totalPaintable.toFloat()) * 100f)
            .coerceIn(0f, 100f)
            .toInt()
    }

    private fun findCollisionPointAfter(end: GridPoint, direction: Direction): GridPoint {
        return when (direction) {
            Direction.UP -> GridPoint(end.row - 1, end.col)
            Direction.DOWN -> GridPoint(end.row + 1, end.col)
            Direction.LEFT -> GridPoint(end.row, end.col - 1)
            Direction.RIGHT -> GridPoint(end.row, end.col + 1)
        }
    }
}

sealed class EngineResult {
    data class Updated(
        val board: GameBoard,
        val traversedPath: List<GridPoint>,
        val collisionAt: GridPoint,
        val collision: Boolean,
        val wonThisMove: Boolean
    ) : EngineResult()

    data class NoOp(
        val board: GameBoard,
        val collision: Boolean = false
    ) : EngineResult()
}
```

Implementation notes:
- Initialize first board by pre-painting start cell if paintable.
- Keep obstacle map immutable per level.
- Trigger win flow exactly once per level (`wonThisMove == true`).

6) UI Specs for each screen

### Splash/Main Screen
- Content:
  - Center logo (`Paint Puzzle`).
  - Large primary `Play` button.
  - Secondary small `Shop` button.
  - Animated flat background (slow-moving gradient clouds/shapes).
- Behavior:
  - On launch: load profile + current level in ViewModel.
  - `Play` navigates to Gameplay with `currentLevel`.
  - No blocking longer than 1.5s; if data ready early, allow immediate play.

### Gameplay Screen
- Top HUD:
  - Progress bar with `%` text.
  - Level indicator (`Level 07`).
  - Gems counter with icon.
- Center:
  - House board rendered via `LazyVerticalGrid`/custom Canvas grid.
  - Sponge sprite tinted by selected skin.
  - Painted trail color from selected skin.
- Input:
  - Swipe detector overlay; only 4-direction dominant axis.
  - On swipe, send `GameplayIntent.Swipe(direction)`.
- Feedback:
  - On collision: tiny splash at collision cell + 80ms shake.
  - On win: freeze input, show confetti, navigate Win after short delay.

### Win Screen
- Content:
  - “House Painted!” headline.
  - Progress fixed at `100%`.
  - Reward card (`+X gems`).
  - `Next Level` button + optional `Replay`.
- Behavior:
  - On enter, gems already granted once.
  - `Next Level` increments and persists current level (cap at 20 then loop/coming-soon).

### Shop Screen
- Content:
  - Gem balance top-right.
  - Skin grid cards (locked/owned/selected states).
  - CTA per card:
    - Locked: `Buy (cost)`
    - Owned not selected: `Select`
    - Selected: `Selected` disabled style
- Rules:
  - Purchase checks gems >= cost.
  - On success: deduct gems, add to owned, auto-select purchased skin.
  - On insufficient gems: show snackbar, no mutation.

7) State Management + Persistence Strategy

- **MVVM contracts**
  - `GameplayUiState`: board model, progress, level, gems, selectedSkin, fx flags.
  - `ShopUiState`: gems, skin items with status enum (`LOCKED/OWNED/SELECTED`).
  - ViewModels expose `StateFlow`; one-shot effects via `SharedFlow`.

- **Persistence (Proto DataStore)**
  - Stored fields:
    - `int32 current_level`
    - `int32 gems`
    - `repeated string owned_skin_ids`
    - `string selected_skin_id`
  - Defaults:
    - `current_level = 1`
    - `gems = 0`
    - `owned_skin_ids = ["classic"]`
    - `selected_skin_id = "classic"`

- **Save points**
  - On win reward granted.
  - On next-level progression.
  - On skin purchase/select.

- **Recovery**
  - Corrupt datastore -> fallback to defaults + non-fatal log.
  - Missing level asset -> fallback to level 1 and log analytics event.

8) Economy & Shop Rules

- **Initial catalog example**
  - `classic` cost 0 (default owned)
  - `sunset` cost 120
  - `mint` cost 180
  - `neon` cost 260
  - `galaxy` cost 350

- **Level rewards (20 levels)**
  - Base reward: `25 + (levelIndex * 3)` gems.
  - First clear only for each level in prototype (track with transient in-memory set now; extend to persisted completion flags in v2).

- **Purchase policy**
  - No duplicate purchases.
  - Deduct then persist atomically in repository transaction function.
  - Auto-select after successful purchase.

- **Balancing target (prototype)**
  - By level ~8 player can buy first premium skin.
  - By level 20 player can unlock 2–3 paid skins with normal progression.

9) Edge Cases + Handling

- Swipe into immediate obstacle/border: no position change, no repaint; still emit collision FX.
- Re-swiping along fully painted corridor: movement valid, painted count unchanged.
- `totalPaintable == 0` (bad level): progress forced 0, level marked invalid and skipped in loader.
- Float precision near completion: use integer painted count, compute percent then clamp; win only if painted count == total.
- Duplicate win trigger from recomposition: win event gated by `wonThisMove` and consumed once.
- App background/restore mid-level: restore board from ViewModel saved state (for prototype, replay from level start acceptable; document behavior).
- Invalid skin id in persistence: fallback to `classic` if missing in catalog.
- Insufficient gems purchase attempt: no mutation, user feedback only.
- Last level completion (20): show `All Levels Complete` state; `Next` loops to level 1 for replay.

10) Testing Checklist

- **Unit tests (core engine)**
  - Slide stops before each obstacle type.
  - Slide stops at border/out-of-bounds.
  - Traversed path paints all paintable cells exactly once.
  - Progress formula equals `paintedPaintable / totalPaintable * 100` (int floor behavior explicit).
  - Win when painted count reaches total, not before.
  - No-op swipe keeps state stable except optional fx flags.

- **Unit tests (economy/shop)**
  - Successful purchase deducts gems and sets owned+selected.
  - Failed purchase (insufficient gems) leaves profile unchanged.
  - Duplicate purchase blocked.

- **Integration tests**
  - Load level asset -> create board -> swipe -> win -> reward -> persist level increment.
  - App relaunch restores gems/skin/current level from datastore.

- **Compose UI tests**
  - Splash Play button navigates to Gameplay.
  - Gameplay progress bar updates after swipe.
  - Win screen displays reward and next-level action.
  - Shop card state toggles Locked→Owned→Selected.

11) 7-Day Implementation Plan

- **Day 1 — Foundation**
  - Setup Compose app shell, navigation graph, theme, dependencies (DataStore, lifecycle, test libs).
  - Define models, enums, and level JSON schema.

- **Day 2 — Engine**
  - Implement `GameEngine`, collision resolver, progress calculator.
  - Write core engine unit tests.

- **Day 3 — Gameplay Feature**
  - Build Gameplay ViewModel + UI state.
  - Implement board rendering + swipe detection + HUD.

- **Day 4 — Win Flow + Economy Core**
  - Implement win detection wiring, reward grant, next level progression.
  - Add confetti/hit feedback hooks.

- **Day 5 — Shop + Persistence**
  - Build shop catalog, purchase/select flows.
  - Integrate Proto DataStore for player profile.

- **Day 6 — Content + Polish**
  - Author/refine 20 level files.
  - Add splash animation, color polish, basic juice effects.

- **Day 7 — QA + Stabilization**
  - Integration and UI tests, bug fixes.
  - Balance gem economy, verify edge cases, prepare prototype build.
