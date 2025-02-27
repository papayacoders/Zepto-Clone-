package com.example.zepto.ui.screens.Components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LocationBar(
    deliveryTime: String = "6 Mins",
    locationText: String = "Your Current Location",
    contentColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Add more top padding to move it down, away from the status bar
            .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = contentColor,
            modifier = Modifier.size(40.dp)
        )

        // Text content with proper spacing
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, top = 2.dp)
        ) {
            Text(
                text = "Delivery In $deliveryTime",
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
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationBarPreview() {
    MaterialTheme {
        LocationBar()
    }
}