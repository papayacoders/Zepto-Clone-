package com.example.zepto.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.zepto.R

@Composable
fun LocationBar(
    deliveryTime: String = "6 Mins",
    locationText: String = "Your Current Location",
    contentColor: Color = Color.White,
            gifResourceId: Int = R.raw.delivery,
    locationgif:Int=R.raw.location
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Add more top padding to move it down, away from the status bar
            .padding(start = 16.dp, end = 16.dp, top = 35.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(locationgif)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = "Location Animation",
            modifier = Modifier.size(80.dp)

            , contentScale = ContentScale.FillBounds
        )

        // Text content with proper spacing
        Column(
            modifier = Modifier

        ) {

                Text(
                    text = "Delivery In $deliveryTime . . . .",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )


            Text(
                text = locationText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(gifResourceId)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = "Location Animation",
            modifier = Modifier.size(50.dp)

                , contentScale = ContentScale.FillBounds
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun LocationBarPreview() {
    MaterialTheme {
        LocationBar()
    }
}