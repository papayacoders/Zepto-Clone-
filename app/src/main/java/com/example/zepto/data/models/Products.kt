package com.example.zepto.data.models

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val weight: Int = 100, // Default weight in grams
    val imageRes: Int = 0,  // Used for local images (backward compatibility)
    val category: String? = null
)



