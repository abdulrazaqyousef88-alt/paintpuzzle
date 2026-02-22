package com.paintpuzzle.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.paintpuzzle.core.data.LevelRepository
import com.paintpuzzle.core.data.PlayerPrefsRepository
import com.paintpuzzle.feature.gameplay.GameplayScreen
import com.paintpuzzle.feature.gameplay.GameplayViewModel
import com.paintpuzzle.feature.shop.ShopScreen
import com.paintpuzzle.feature.shop.ShopViewModel
import com.paintpuzzle.feature.splash.SplashScreen
import com.paintpuzzle.feature.splash.SplashViewModel
import com.paintpuzzle.feature.win.WinScreen
import com.paintpuzzle.feature.win.WinViewModel

@Composable
fun AppNavHost(appContext: Context) {
    val navController = rememberNavController()
    val playerRepo = PlayerPrefsRepository(appContext)
    val levelRepo = LevelRepository()

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            val vm: SplashViewModel = viewModel(factory = SimpleVmFactory { SplashViewModel(playerRepo) })
            val profile by vm.profile.collectAsState()
            LaunchedEffect(Unit) { vm.load() }
            SplashScreen(
                onPlay = {
                    if (profile != null) navController.navigate(Routes.Gameplay)
                },
                onShop = { navController.navigate(Routes.Shop) }
            )
            LaunchedEffect(profile) {
                if (profile != null) {
                    navController.navigate(Routes.Gameplay)
                }
            }
        }

        composable(Routes.Gameplay) {
            val splashVm: SplashViewModel = viewModel(factory = SimpleVmFactory { SplashViewModel(playerRepo) })
            val profile by splashVm.profile.collectAsState()
            LaunchedEffect(Unit) { splashVm.load() }

            val vm: GameplayViewModel = viewModel(factory = SimpleVmFactory { GameplayViewModel(playerRepo, levelRepo) })
            val state by vm.state.collectAsState()

            LaunchedEffect(profile) {
                profile?.let { vm.bootstrap(it) }
            }

            state?.let {
                GameplayScreen(
                    state = it.ui,
                    onSwipe = { dir -> vm.onSwipe(dir) { reward -> navController.navigate(Routes.win(reward)) } },
                    onShop = { navController.navigate(Routes.Shop) },
                    onClearFx = vm::clearFx
                )
            }
        }

        composable(
            route = Routes.Win,
            arguments = listOf(navArgument("reward") { type = NavType.IntType })
        ) {
            val reward = it.arguments?.getInt("reward") ?: 0
            val vm: WinViewModel = viewModel(factory = SimpleVmFactory { WinViewModel(playerRepo) })
            WinScreen(
                reward = reward,
                onNextLevel = {
                    vm.nextLevel {
                        navController.navigate(Routes.Gameplay) {
                            popUpTo(Routes.Splash)
                        }
                    }
                }
            )
        }

        composable(Routes.Shop) {
            val vm: ShopViewModel = viewModel(factory = SimpleVmFactory { ShopViewModel(playerRepo) })
            val state by vm.state.collectAsState()
            LaunchedEffect(Unit) { vm.load() }
            state?.let {
                ShopScreen(
                    state = it,
                    onClickSkin = vm::buyOrSelect,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
