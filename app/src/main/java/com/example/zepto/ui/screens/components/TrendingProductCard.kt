package com.example.zepto.ui.screens.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.zepto.R
import com.example.zepto.data.models.Product

private const val TAG = "TrendingProductCard"

@Composable
fun TrendingProductCard(product: Product,
                        onCategoryClick: (String) -> Unit) {
    LaunchedEffect(product.id) {
        Log.d(TAG, "Rendering TrendingProductCard for product id=${product.id}, name=${product.name}")
    }

    var isFavorite by remember { mutableStateOf(false) }

    val discountPercentage = when (product.category?.lowercase()) {
        "electronics" -> "UP TO 90% OFF"
        "jewelery" -> "UP TO 85% OFF"
        "men's clothing" -> "UP TO 80% OFF"
        "women's clothing" -> "UP TO 80% OFF"
        else -> "UP TO 80% OFF"
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(.8f)
            .clip(RoundedCornerShape(16.dp))
            .clickable {  product.category?.let { category -> onCategoryClick(category) } },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Image fills the entire card
            Image(
                painter = rememberImagePainter(
                    data = product.imageUrl ?: "",
                    builder = {
                        placeholder(R.drawable.shopping_bag_svgrepo_com)
                        error(R.drawable.shopping_bag_svgrepo_com)
                        crossfade(true)
                    }
                ),
                contentDescription = product.name ?: "Product Image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.fillMaxSize()
            )

            // Discount banner with glass effect (Top)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .graphicsLayer(alpha = 0.95f)
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF000000).copy(alpha = 0.7f),
                                Color(0xFF000000).copy(alpha = 0.4f)
                            )
                        )
                    )
                    .blur(radius = 0.5.dp)
            ) {
                Text(
                    text = discountPercentage,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 8.dp)
                )
            }

            // Category info with glass effect (Bottom)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .graphicsLayer(alpha = 0.95f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF000000).copy(alpha = 0.4f),
                                Color(0xFF000000).copy(alpha = 0.7f)
                            )
                        )
                    )
                    .blur(radius = 0.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    Arrangement.Center
                ) {
                    Text(
                        text = formatCategoryName(product.category ?: "General"),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    }
                }



        }
    }
}

// Helper function to format category names
private fun formatCategoryName(category: String): String {
    return when (category.lowercase()) {
        "electronics" -> "Electronics"
        "jewelery" -> "Jewelry"
        "men's clothing" -> "Men's Fashion"
        "women's clothing" -> "Women's Fashion"
        else -> category.split(" ").joinToString(" ") { it.capitalize() }
    }
}