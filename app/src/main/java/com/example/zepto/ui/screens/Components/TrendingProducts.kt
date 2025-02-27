package com.example.zepto.ui.screens.Components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zepto.R
import com.example.zepto.data.models.Product
import com.example.zepto.ui.screens.getCategoryGradient
import com.example.zepto.ui.viewmodels.HomeViewModel

private const val TAG = "TrendingProductsSection"

@Composable
fun TrendingProductsSection(products: List<Product>) {
    val viewModel: HomeViewModel = viewModel()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Debug logging
    LaunchedEffect(products) {
        Log.d(TAG, "TrendingProductsSection received ${products.size} products")
        products.forEachIndexed { index, product ->
            Log.d(TAG, "Product $index: id=${product.id}, name=${product.name}, " +
                    "category=${product.category}, imageUrl=${product.imageUrl}, imageRes=${product.imageRes}")
        }
    }

    // Fix image URLs if needed before displaying
    LaunchedEffect(Unit) {
        Log.d(TAG, "Calling fixProductImageUrls from TrendingProductsSection")
        viewModel.fixProductImageUrls()
    }

    val categoryBackground = selectedCategory?.let {
        getCategoryGradient(it)
    } ?: Brush.horizontalGradient(listOf(Color(0xFF6200EE), Color(0xFF3700B3))) // Default purple gradient

    // Ensure we have 6 products with appropriate categories
    val displayProducts = remember(products) {
        // Your specific categories
        val categories = listOf("electronics", "jewelery", "men's clothing", "women's clothing")

        if (products.isEmpty()) {
            // Create dummy products if no products are available
            Log.d(TAG, "Creating dummy products because product list is empty")
            List(6) { index ->
                val categoryIndex = index % categories.size
                val imageRes = when (categoryIndex) {
                    0 -> R.drawable.headphones_round_svgrepo_com
                    1 -> R.drawable.wedding_relationships_couple_marry_svgrepo_com
                    2 -> R.drawable.faishon
                    3 -> R.drawable.beauty
                    else -> R.drawable.shopping_bag_svgrepo_com
                }

                Log.d(TAG, "Created dummy product $index with imageRes=$imageRes")

                Product(
                    id = index,
                    name = "Product $index",
                    price = 299.0,
                    category = categories[categoryIndex],
                    imageUrl = "", // Empty image URL will use fallback
                    imageRes = imageRes
                )
            }
        } else if (products.size < 6) {
            // Reuse products to fill the 6 spots
            Log.d(TAG, "Not enough products (${products.size}), filling to 6")
            val result = mutableListOf<Product>()
            repeat(6) { index ->
                val product = products[index % products.size]
                // Assign different categories to ensure variety
                val category = categories[index % categories.size]
                val modifiedProduct = product.copy(category = category)
                result.add(modifiedProduct)
                Log.d(TAG, "Added product ${modifiedProduct.id} with category $category")
            }
            result
        } else {
            // We have enough products, try to get one from each category if possible
            Log.d(TAG, "Using actual products, grouping by category")
            val groupedByCategory = products.groupBy { it.category?.lowercase() ?: "other" }
            val result = mutableListOf<Product>()

            // First add one from each available category
            categories.forEach { category ->
                groupedByCategory[category]?.firstOrNull()?.let {
                    result.add(it)
                    Log.d(TAG, "Added product ${it.id} from category $category")
                }
            }

            // If we still need more, add remaining products
            if (result.size < 6) {
                val remaining = products.filter { it !in result }
                val additionalProducts = remaining.take(6 - result.size)
                result.addAll(additionalProducts)
                Log.d(TAG, "Added ${additionalProducts.size} additional products to reach desired count")
            }

            // If we somehow still don't have 6, repeat products
            if (result.size < 6) {
                val moreProducts = mutableListOf<Product>()
                repeat(6 - result.size) { index ->
                    val product = result[index % result.size]
                    moreProducts.add(product)
                    Log.d(TAG, "Repeated product ${product.id} to reach desired count")
                }
                result.addAll(moreProducts)
            }

            // Take exactly 6
            val finalProducts = result.take(6)
            Log.d(TAG, "Final products for display: ${finalProducts.map { it.id }}")
            finalProducts
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = categoryBackground
            )
            .padding(16.dp)
    ) {
        // Super Sonic Deals Header
        Text(
            text = "Super Sonic",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "DEALS",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // First row - 3 items
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayProducts.take(3).forEach { product ->
                Box(modifier = Modifier.weight(1f)) {
                    TrendingProductCard(product)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Second row - 3 items
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayProducts.drop(3).take(3).forEach { product ->
                Box(modifier = Modifier.weight(1f)) {
                    TrendingProductCard(product)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Promo code banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.4F)
            )
        ) {
            Text(

                text = "Use FLAT20 - Get 20% off on purchase of ₹3000",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)

            )
        }
    }
}