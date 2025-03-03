package com.example.zepto.ui.screens.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.zepto.data.models.Category


@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        // Category icon
        Image(
            painter = painterResource(category.imageRes),
            contentDescription = category.name,
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )

        Spacer(modifier = Modifier.height(1.dp))

        // Category name - fixed height container to prevent shifting
        Box(
            modifier = Modifier.height(35.dp)
            , // Fixed height for text container
            contentAlignment = Alignment.Center,

        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Selected indicator - keep same width but change visibility
        Box(
            modifier = Modifier
                .width(60.dp) // Fixed width for all items
                .height(3.dp)
                .background(if (isSelected) Color.White else Color.Transparent)
        )
    }
}

