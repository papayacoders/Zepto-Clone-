package com.example.zepto.ui.screens

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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMotionApi::class)
@Composable
fun ZeptoCloneApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val bottomBarState = remember { mutableStateOf(0f) }

    // Access the view model to get the selected category
    val viewModel: HomeViewModel = viewModel()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Get the category background gradient - using the same function that's defined elsewhere
    val categoryBackground = selectedCategory?.let {
        getCategoryGradient(it)
    } ?: Brush.verticalGradient(listOf(
        Color(0xFF191970), // Midnight blue 
        Color(0xFF4682B4)  // Steel blue - default color
    ))

    // Create the MotionScene content string
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
                bottom: ["parent", "bottom", 80],
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
                    translationY: [0, 80]
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

        // Make status bar transparent
        window.statusBarColor = Color.Transparent.toArgb()

        // Make status bar icons light (white)
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false

        // Make sure system bars draw over app content
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(categoryBackground)) {
        MotionLayout(
            modifier = Modifier.fillMaxSize(),
            motionScene = motionScene,
            progress = bottomBarState.value
        ) {
            // The content area (including Scaffold)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Scaffold(
                    // Use safeDrawing insets to properly handle system bars
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        // Empty placeholder for the actual bottom bar
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                ) { paddingValues ->
                    // Content
                    when (selectedTab) {
                        0 -> HomeScreen(
                            // Don't add top padding here - this allows our content to go edge-to-edge
                            // and the background color to extend behind the status bar
                            paddingValues = PaddingValues(
                                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                                bottom = paddingValues.calculateBottomPadding()
                            ),
                            onScroll = { direction ->
                                // If scrolling up (direction < 0), hide the bar (progress = 1f)
                                // If scrolling down (direction > 0), show the bar (progress = 0f)
                                if (direction < 0) {
                                    // Scrolling up - hide the bar
                                    bottomBarState.value = 1f
                                } else if (direction > 0) {
                                    // Scrolling down - show the bar
                                    bottomBarState.value = 0f
                                }
                                // When direction = 0, maintain current state
                            }
                        )
                        1 -> SearchScreen(paddingValues)
                        2 -> CartScreen(paddingValues)
                        3 -> AccountScreen(paddingValues)
                    }
                }
            }

            // The actual bottom bar that will be animated by MotionLayout
            NavigationBar(
                modifier = Modifier
                    .layoutId("bottomBar")
                    .shadow(8.dp)
                    .zIndex(10f),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Search, "Search") },
                    label = { Text("Search") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.ShoppingCart, "Cart") },
                    label = { Text("Cart") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, "Account") },
                    label = { Text("Account") }
                )
            }
        }
    }
}

@Composable
fun AccountScreen(paddingValues: PaddingValues) {

}

@Composable
fun CartScreen(paddingValues: PaddingValues) {

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
                Color(0xFF8B4513), // Darker brown
                Color(0xFFDEB887)  // Lighter burlywood
            )
        )
        "fresh" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF006400), // Dark green
                Color(0xFF90EE90)  // Light green
            )
        )
        "electronics" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF00008B), // Dark blue
                Color(0xFF4169E1)  // Royal blue
            )
        )
        "home" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFA0522D), // Sienna
                Color(0xFFE6C9A8)  // Light tan
            )
        )
        "women's clothing" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFC71585), // Deep pink
                Color(0xFFFFC0CB)  // Light pink
            )
        )
        "fashion" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF800080), // Dark purple
                Color(0xFFDA70D6)  // Orchid
            )
        )
        "men's clothing" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFB8860B), // Dark goldenrod
                Color(0xFFFFE4B5)  // Moccasin
            )
        )
        "wedding store" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFC71585), // Medium violet red
                Color(0xFFFFB6C1)  // Light pink
            )
        )
        "toys" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF4500), // Orange red
                Color(0xFFFFDAB9)  // Peach puff
            )
        )
        "all" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF191970), // Midnight blue
                Color(0xFF4682B4)  // Steel blue
            )
        )
        "cafe" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3E2723), // Dark brown
                Color(0xFFCCB195)  // Light beige
            )
        )
        "beauty" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF1493), // Deep pink
                Color(0xFFFFB6C1)  // Light pink
            )
        )
        "deal zone" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFB8860B), // Dark gold
                Color(0xFFFFE4B5)  // Moccasin
            )
        )
        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF666666), // Dark gray
                Color(0xFFEEEEEE)  // Light gray
            )
        )
    }
}