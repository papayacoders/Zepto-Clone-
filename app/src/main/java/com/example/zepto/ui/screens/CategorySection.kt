package com.example.zepto.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.zepto.data.models.Category
import com.example.zepto.ui.screens.Components.CategoryItem

@Composable
fun CategoriesSection(
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    selectedCategory: Category?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column {
            // Categories list
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( bottom = 2.dp)
            ) {
                categories.forEach { category ->
                    CategoryItem(
                        category = category,
                        onClick = { onCategorySelected(category) },
                        isSelected = category.id == selectedCategory?.id
                    )
                }
            }

            // White tab indicator line at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White)
            )
        }
    }
}
