package com.example.zepto.ui.screens.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.decode.GifDecoder
import com.example.zepto.R

@Composable
fun GifImage(
    modifier: Modifier = Modifier
) {

    val gifUrl = R.raw.vd

    // Animation for subtle pulsing effect
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val scale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
       ,
        contentAlignment = Alignment.Center
    ) {
        // Main GIF with enhanced styling
        Card(
            modifier = Modifier
                .fillMaxWidth() // Slightly less than full width for aesthetic padding

                .graphicsLayer {
                    clip = true

                },


            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // GIF Image with animation and fade effect
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gifUrl)
                        .decoderFactory(GifDecoder.Factory())
                        .build(),
                    contentDescription = "Promotional Animation",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp) // Fixed height for better proportions
                        .scale(scale) // Subtle pulse animation

                )

//                // Promotional overlay text (optional - uncomment if needed)
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(bottom = 16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "FLASH SALE",
//                        fontFamily = FasterOne,
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = "Up to 70% OFF",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = Color.White
//                    )
//                }
            }
        }
    }
}