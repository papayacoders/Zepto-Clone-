package com.example.zepto.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.zepto.R
import com.example.zepto.data.models.Product
import com.example.zepto.ui.viewmodels.CartViewModel

@Composable
fun ProductCard(
    product:Product,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val discountPercentage = remember { (Math.random() * 60 + 20).toInt() }
    val rating = remember { (3.5 + Math.random() * 1.5) }
    val reviewCount = remember { (Math.random() * 1000).toInt() + 50 }

    // Get current quantity from cart for this product
    val cartItems by cartViewModel.cartItems.collectAsState()
    val quantity = cartItems.find { it.product.id == product.id }?.quantity ?: 0

    // Styled shapes and colors
    val discountBadgeShape = RoundedCornerShape(
        topStart = 12.dp,
        bottomEnd = 12.dp,
        topEnd = 0.dp,
        bottomStart = 0.dp
    )

    val addButtonShape = RoundedCornerShape(50)  // Pill shape for the add button
    val addButtonColor = Color(0xFFE20F4B)  // Pink color from screenshot

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column {
            // Image section with Add button overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                // Product Image
                if (product.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(product.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.shopping_bag_svgrepo_com)
                    )
                } else if (product.imageRes != 0) {
                    Image(
                        painter = painterResource(product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image")
                    }
                }

                // Discount badge - redesigned to match screenshot
                if (product.price < 300) {
                    Box(
                        modifier = Modifier
                            .padding(start = 0.dp, top = 0.dp)
                            .align(Alignment.TopStart)
                            .clip(discountBadgeShape)
                            .background(Color(0xFF8E24AA)) // Purple color from screenshot
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${discountPercentage}% Off",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Add/Minus button (toggles between plus and a quantity selector with +/- buttons)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    if (quantity == 0) {
                        // Add button
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = addButtonColor,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    // Add product to cart
                                    cartViewModel.addToCart(product)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add to cart",
                                tint = addButtonColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        // Quantity controller
                        Row(
                            modifier = Modifier
                                .height(32.dp)
                                .clip(addButtonShape)
                                .background(addButtonColor),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Minus button
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        // Remove product from cart
                                        cartViewModel.removeFromCart(product)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "−",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Quantity display
                            Box(
                                modifier = Modifier
                                    .width(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$quantity",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Plus button
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        // Add product to cart
                                        cartViewModel.addToCart(product)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Product details
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                // Product name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Product piece info
                Text(
                    text = "1 l", // Matching the screenshot format
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Star rating - new addition to match screenshot
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Star rating background in green with rating number
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF0D7148))  // Dark green color from screenshot
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = "${rating.toString().take(3)}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Review count in parentheses
                    Text(
                        text = "($reviewCount)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Price section with original and discounted prices
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rupee symbol
                    Text(
                        text = "₹",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Current price
                    Text(
                        text = "${product.price.toInt()}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Space between prices
                    Spacer(modifier = Modifier.width(4.dp))

                    // Original price (strikethrough)
                    if (product.price < 300) {
                        Text(
                            text = "₹${(product.price * 1.4).toInt()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}