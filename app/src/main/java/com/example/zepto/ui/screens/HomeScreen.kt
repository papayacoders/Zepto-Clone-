package com.example.zepto.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zepto.ui.screens.components.LocationBar
import com.example.zepto.ui.screens.components.ProductCard
import com.example.zepto.ui.screens.components.SearchBar
import com.example.zepto.ui.screens.components.TrendingProductsSection
import com.example.zepto.ui.viewmodels.HomeViewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zepto.ui.viewmodels.CartViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMotionApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onNavigateToCategory: (String) -> Unit,
    onScroll: (Float) -> Unit
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val viewModel: HomeViewModel = viewModel()
    val scrollState = rememberLazyListState()
    val categories by viewModel.categories.collectAsState()
    val products by viewModel.products.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    // Create a custom nested scroll connection to detect scroll direction
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Pass just the raw y value - no additional processing
                onScroll(available.y)
                return Offset.Zero // Don't consume the scroll
            }
        }
    }
    // More efficient scroll direction detection with thresholds
    val scrollDirection = remember { mutableStateOf(0f) }

    LaunchedEffect(scrollState) {
        snapshotFlow {
            // Only track substantial changes by using a higher scale factor
            scrollState.firstVisibleItemIndex * 200 + scrollState.firstVisibleItemScrollOffset / 5
        }
            .distinctUntilChanged { old, new -> Math.abs(old - new) < 20 } // Ignore small changes
            .map { currentOffset ->
                val previousOffset = scrollDirection.value
                when {
                    currentOffset > previousOffset + 25 -> 1f  // Significant downward scroll
                    currentOffset < previousOffset - 25 -> -1f // Significant upward scroll
                    else -> scrollDirection.value        // No significant change
                }
            }
            .collect { direction ->
                if (direction != scrollDirection.value) {
                    scrollDirection.value = direction
                    onScroll(direction)
                }
            }
    }

    // Set "All" as default category
    LaunchedEffect(Unit) {
        if (selectedCategory == null && categories.isNotEmpty()) {
            val allCategory = categories.find { it.name.equals("All", ignoreCase = true) }
                ?: categories.first()
            viewModel.selectCategory(allCategory)
        }
    }

    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && selectedCategory == null) {
            val allCategory = categories.find { it.name.equals("All", ignoreCase = true) }
                ?: categories.first()
            viewModel.selectCategory(allCategory)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fixProductImageUrls()
    }

    // Optimize scroll progress calculation with better thresholds
    // and memoize it to prevent recalculation
    val scrollProgress by remember {
        derivedStateOf {
            when {
                scrollState.firstVisibleItemIndex > 0 -> 1f
                scrollState.firstVisibleItemScrollOffset > 0 -> {
                    // Make this even smoother by using a larger denominator
                    (scrollState.firstVisibleItemScrollOffset / 1000f).coerceIn(0f, 1f)
                }
                else -> 0f
            }
        }
    }

    // Apply smoother animation with optimized timing
    val animatedScrollProgress by animateFloatAsState(
        targetValue = scrollProgress,
        animationSpec = tween(
            durationMillis = 350, // Longer animation for smoother appearance
            easing = FastOutLinearInEasing
        ),
        label = "scroll"
    )

    // Calculate the category background only when selected category changes
    val categoryBackground = remember(selectedCategory) {
        selectedCategory?.let {
            getCategoryGradient(it)
        } ?: Brush.horizontalGradient(listOf(Color.White, Color.White))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            // Apply background here to prevent redrawing during scrolling
            .background(categoryBackground)
            .nestedScroll(nestedScrollConnection)
    ) {
        MotionLayout(
            start = homeScreenStartConstraintSet(),
            end = homeScreenEndConstraintSet(),
            progress = animatedScrollProgress,
            modifier = Modifier
                .fillMaxSize()
                // Use graphicsLayer for better hardware acceleration
                .graphicsLayer(
                    renderEffect = null, // Remove the custom shader which might be causing issues
                    alpha = 0.99f, // Force hardware acceleration
                )
        ) {
            // Location Bar
            Box(
                modifier = Modifier
                    .layoutId("location_bar")
                    .background(Color.Transparent)
                    .zIndex(1f)
            ) {
                LocationBar(contentColor = Color.White)
            }

            // Search Bar
            Box(
                modifier = Modifier
                    .layoutId("search_bar")
                    .background(Color.Transparent)
                    .zIndex(0.9f)
            ) {
                SearchBar()
            }

            // Categories Section
            Box(
                modifier = Modifier
                    .layoutId("category_section")
                    .background(Color.Transparent)
                    .zIndex(0.8f)
            ) {
                CategoriesSection(
                    categories = categories,
                    onCategorySelected = { category ->
                        viewModel.selectCategory(category)
                    },
                    selectedCategory = selectedCategory
                )
            }

            // Main Content Area
            Box(
                modifier = Modifier
                    .layoutId("main_content")
                    .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .zIndex(0.7f)
            ) {
                if (isLoading && products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (error != null && products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(error ?: "Unknown error occurred")
                    }
                } else {
                    // Optimized LazyColumn
                    LazyColumn(
                        state = scrollState,
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),

                    ) {
                        // Trending Section
                        item(key = "trending") {
                            TrendingProductsSection(products = products,  onNavigateToCategory = onNavigateToCategory, viewModel = viewModel)
                        }

                        // Product Grid Header


                        // Product Grid with optimized keys
                        items(
                            items = products.chunked(3),
                            key = { it.firstOrNull()?.id ?: "empty-${System.identityHashCode(it)}" }
                        ) { rowProducts ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (product in rowProducts) {
                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        ProductCard(product,
                                            cartViewModel = cartViewModel)
                                    }
                                }

                                // Fill empty spots with blank spaces
                                repeat(3 - rowProducts.size) {
                                    Box(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
fun homeScreenStartConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val locationBar = createRefFor("location_bar")
        val searchBar = createRefFor("search_bar")
        val categorySection = createRefFor("category_section")
        val mainContent = createRefFor("main_content")

        constrain(locationBar) {
            width = Dimension.fillToConstraints
            height = Dimension.value(95.dp) // Increased height to accommodate more space
            top.linkTo(parent.top, margin = 12.dp) // Added a top margin to push it down from status bar
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(searchBar) {
            width = Dimension.fillToConstraints
            height = Dimension.value(60.dp)
            top.linkTo(locationBar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(categorySection) {
            width = Dimension.fillToConstraints
            height = Dimension.value(100.dp)
            top.linkTo(searchBar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(mainContent) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(categorySection.bottom, margin = (-17).dp) // Overlap with category
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
}

fun homeScreenEndConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val locationBar = createRefFor("location_bar")
        val searchBar = createRefFor("search_bar")
        val categorySection = createRefFor("category_section")
        val mainContent = createRefFor("main_content")

        constrain(locationBar) {
            width = Dimension.fillToConstraints
            height = Dimension.value(75.dp) // Increased height from 56.dp to 75.dp
            top.linkTo(parent.top, margin = 15.dp) // Added a top margin
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            alpha = 0f
        }

        constrain(searchBar) {
            width = Dimension.fillToConstraints
            height = Dimension.value(70.dp)
            top.linkTo(parent.top, margin = 30.dp) // Adjusted to position at top when scrolled
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(categorySection) {
            width = Dimension.fillToConstraints
            height = Dimension.value(100.dp)
            top.linkTo(searchBar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(mainContent) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(categorySection.bottom, margin = (-17).dp) // Keep the overlap when scrolled
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
}