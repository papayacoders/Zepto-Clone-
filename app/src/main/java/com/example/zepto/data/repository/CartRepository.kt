package com.example.zepto.data.repository

import com.example.zepto.data.models.CartItem
import com.example.zepto.data.models.Product
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Cart Repository
 */
interface CartRepository {
    /**
     * Get all items in the cart
     */
    fun getCartItems(): Flow<List<CartItem>>

    /**
     * Get the total number of items in the cart
     */
    fun getTotalItems(): Flow<Int>

    /**
     * Get the total price of items in the cart
     */
    fun getTotalPrice(): Flow<Double>

    /**
     * Add a product to the cart
     */
    suspend fun addToCart(product: Product)

    /**
     * Remove a product from the cart
     */
    suspend fun removeFromCart(product: Product)

    /**
     * Set a specific quantity for a product
     */
    suspend fun setQuantity(product: Product, quantity: Int)

    /**
     * Get the quantity of a specific product in the cart
     */
    suspend fun getQuantity(productId: Int): Int

    /**
     * Clear the entire cart
     */
    suspend fun clearCart()
}