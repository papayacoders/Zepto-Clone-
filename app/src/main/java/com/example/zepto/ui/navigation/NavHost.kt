package com.example.zepto.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.zepto.ui.screens.AccountScreen
import com.example.zepto.ui.screens.CartScreen
import com.example.zepto.ui.screens.CategoryScreen
import com.example.zepto.ui.screens.HomeScreen
import com.example.zepto.ui.screens.SearchScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ZeptoNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onScroll: (Float) -> Unit,
    onBottomBarVisibilityChange: (Boolean) -> Unit
) {
    // This helps us track which tab we're in
    val currentTab = remember { mutableStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = ZeptoDestinations.Home.route
    ) {
        // Home tab
        composable(ZeptoDestinations.Home.route) {
            currentTab.value = 0
            onBottomBarVisibilityChange(true)

            HomeScreen(
                paddingValues = PaddingValues(
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = paddingValues.calculateBottomPadding()
                ),
                onScroll = onScroll,
                onNavigateToCategory = { categoryId ->
                    // Navigate to category screen
                    navController.navigate(ZeptoDestinations.CategoryDetail.createRoute(categoryId))
                }
            )
        }

        // Search tab
        composable(ZeptoDestinations.Search.route) {
            currentTab.value = 1
            onBottomBarVisibilityChange(true)

            SearchScreen(paddingValues = paddingValues)
        }

        // Cart tab
        composable(ZeptoDestinations.Cart.route) {
            currentTab.value = 2
            onBottomBarVisibilityChange(false) // Hide bottom bar in cart

            CartScreen(
                paddingValues = paddingValues,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Account tab
        composable(ZeptoDestinations.Account.route) {
            currentTab.value = 3
            onBottomBarVisibilityChange(true)

            AccountScreen(paddingValues = paddingValues)
        }

        // Category detail screen
        composable(
            route = ZeptoDestinations.CategoryDetail.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Keep bottom bar visible on category screens
            onBottomBarVisibilityChange(true)

            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryScreen(
                categoryId = categoryId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}