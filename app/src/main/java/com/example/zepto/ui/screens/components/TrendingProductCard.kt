package com.example.zepto.ui.screens.Components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.zepto.R
import com.example.zepto.data.models.Product

private const val TAG = "TrendingProductCard"

@Composable
fun TrendingProductCard(product: Product) {
    LaunchedEffect(product.id) {
        Log.d(TAG, "Rendering TrendingProductCard for product id=${product.id}, name=${product.name}")
    }

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
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image fills the entire card
            Image(
                painter = rememberImagePainter(
                    data = product.imageUrl ?: "",
                    builder = {
                        placeholder(R.drawable.shopping_bag_svgrepo_com)
                        error(R.drawable.shopping_bag_svgrepo_com)
                    }
                ),
                contentDescription = product.name ?: "Product Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Discount text (Top Center)
            Text(
                text = discountPercentage,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()

                    .align(Alignment.TopCenter)
                    .background(Color.Black.copy(alpha = 0.8F))
                    .padding(vertical = 4.dp)
            )

            // Category text (Bottom Center)
            Text(
                text = formatCategoryName(product.category ?: "General"),
                color = Color.White,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()

                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.8F))
                    .padding(vertical = 6.dp)
            )
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