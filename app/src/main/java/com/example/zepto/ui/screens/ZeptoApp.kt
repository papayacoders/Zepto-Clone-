package com.example.zepto.ui.screens


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zepto.data.models.Category
import com.example.zepto.ui.viewmodels.HomeViewModel
import androidx.compose.foundation.layout.WindowInsets

import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.animateFloatAsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.zepto.ui.navigation.ZeptoDestinations
import com.example.zepto.ui.navigation.ZeptoNavGraph
import com.example.zepto.ui.screens.components.NavigationItem


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMotionApi::class)
@Composable
fun ZeptoCloneApp() {
    var selectedTab by remember { mutableStateOf(0) }
    // Remember previous tab to handle navigation
    var previousTab by remember { mutableStateOf(0) }

    // Use rememberNavController to create a NavHostController
    val navController = rememberNavController()

    // Create a state to track bottom bar visibility with a default of visible
    val bottomBarVisible = remember { mutableStateOf(true) }

    // Create a debounced coroutine scope for handling scroll events with slight delay
    val coroutineScope = rememberCoroutineScope()
    var showHideJob: Job? = remember { null }

    // Hide bottom bar when cart screen is active
    LaunchedEffect(selectedTab) {
        // If we're navigating to the cart screen (tab 2), hide the bottom bar
        if (selectedTab == 2) {
            bottomBarVisible.value = false
            // Remember the previous tab for back navigation
            if (previousTab != 2) {
                previousTab = selectedTab
            }
        } else {
            // For any other screen, show the bottom bar
            bottomBarVisible.value = true
            // Update previous tab
            previousTab = selectedTab
        }
    }

    // Navigation handler - update the navigation controller when tab changes
    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> navController.navigate(ZeptoDestinations.Home.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
            1 -> navController.navigate(ZeptoDestinations.Search.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
            2 -> navController.navigate(ZeptoDestinations.Cart.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
            3 -> navController.navigate(ZeptoDestinations.Account.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    // Animation for the bottom bar
    val bottomBarOffset by animateFloatAsState(
        targetValue = if (bottomBarVisible.value) 0f else 1f,
        animationSpec = tween(
            durationMillis = if (bottomBarVisible.value) 200 else 150,
            easing = if (bottomBarVisible.value) FastOutSlowInEasing else LinearOutSlowInEasing
        ),
        label = "BottomBarAnimation"
    )

    // Access the view model to get the selected category
    val viewModel: HomeViewModel = viewModel()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Get the category background gradient
    val categoryBackground = selectedCategory?.let {
        getCategoryGradient(it)
    } ?: Brush.verticalGradient(listOf(
        Color(0xFF191970), // Midnight blue
        Color(0xFF4682B4)  // Steel blue - default color
    ))

    // Motion scene for animation
    val motionSceneContent = """
        {
          ConstraintSets: {
            start: {
              bottomBar: {
                width: "spread",
                height: 80,
                bottom: ["parent", "bottom", 0],
                start: ["parent", "start", 0],
                end: ["parent", "end", 0]
              }
            },
            end: {
              bottomBar: {
                width: "spread",
                height: 80,
                bottom: ["parent", "bottom", -80],
                start: ["parent", "start", 0],
                end: ["parent", "end", 0]
              }
            }
          },
          Transitions: {
            default: {
              from: "start",
              to: "end",
              KeyFrames: {
                KeyAttributes: [
                  {
                    target: ["bottomBar"],
                    frames: [0, 100],
                    translationY: [0, 80],
                    easing: "cubic(0.2, 0.0, 0.0, 1.0)"
                  }
                ]
              }
            }
          }
        }
    """

    // Create the MotionScene
    val motionScene = MotionScene(content = motionSceneContent)

    // Configure edge-to-edge display and transparent status bar
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    // Function to navigate back from cart to previous screen (usually home)
    val navigateBack: () -> Unit = {
        // Force select home tab if previous tab was cart
        selectedTab = if (previousTab == 2) 0 else previousTab
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        MotionLayout(
            modifier = Modifier.fillMaxSize(),
            motionScene = motionScene,
            progress = bottomBarOffset
        ) {
            // The content area (including Scaffold)
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        // Empty placeholder
                    }
                ) { paddingValues ->
                    // The NavHost goes here
                    ZeptoNavGraph(
                        navController = navController,
                        paddingValues = paddingValues,
                        onScroll = { direction ->
                            // Cancel any pending jobs to prevent rapid toggling
                            showHideJob?.cancel()
                            showHideJob = coroutineScope.launch {
                                if (direction > 0.5f) {
                                    bottomBarVisible.value = true
                                } else if (direction < -0.5f) {
                                    bottomBarVisible.value = false
                                }
                            }
                        },
                        onBottomBarVisibilityChange = { visible ->
                            bottomBarVisible.value = visible
                        }
                    )
                }
            }

            // The actual bottom bar that will be animated by MotionLayout
            Box(
                modifier = Modifier
                    .layoutId("bottomBar")
                    .zIndex(10f)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavigationItem(
                            icon = Icons.Default.Home,
                            label = "Home",
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )

                        NavigationItem(
                            icon = Icons.Default.Search,
                            label = "Search",
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )

                        NavigationItem(
                            icon = Icons.Default.ShoppingCart,
                            label = "Cart",
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 }
                        )

                        NavigationItem(
                            icon = Icons.Default.Person,
                            label = "Account",
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountScreen(paddingValues: PaddingValues) {

}

@Composable
fun SearchScreen(paddingValues: PaddingValues) {

}
// Redefining the getCategoryGradient function here for completeness
// This is functionally identical to the one you already have







fun getCategoryGradient(category: Category): Brush {
    return when (category.name.lowercase()) {
        "jewelery" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFC89132), // Darker gold
                Color(0xFFAA7714)  // Medium amber gold
            )
        )

        "electronics" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0768B8), // Deep navy blue
                Color(0xFF023879)
            )
        )

        "women's clothing" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF385C), // Vibrant pink/red
                Color(0xFFFF89A3)  // Light pink
            )
        )

        "men's clothing" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A8339), // Darker green
                Color(0xFF146C2F)  // Medium forest green
            )
        )

        "all" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF5521AB), // Zepto's brand purple (primary)
                Color(0xFF8C4FFE)  // Secondary purple
            )
        )

        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF9A0D), // Zepto orange
                Color(0xFFFFCC80)  // Light orange
            )
        )
    }
}