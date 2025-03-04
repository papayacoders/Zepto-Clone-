package com.example.zepto.data.repository

import com.example.zepto.data.models.CartItem
import com.example.zepto.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of CartRepository that stores cart data in memory
 */
@Singleton
class CartRepositoryImpl @Inject constructor() : CartRepository {

    // StateFlow to hold the current cart items
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    /**
     * Get all items in the cart
     */
    override fun getCartItems(): Flow<List<CartItem>> = _cartItems.asStateFlow()

    /**
     * Get the total number of items in the cart
     */
    override fun getTotalItems(): Flow<Int> = _cartItems.map { items ->
        items.sumOf { it.quantity }
    }

    /**
     * Get the total price of items in the cart
     */
    override fun getTotalPrice(): Flow<Double> = _cartItems.map { items ->
        items.sumOf { it.quantity * it.product.price }
    }

    /**
     * Add a product to cart or increment its quantity if already present
     */
    override suspend fun addToCart(product: Product) {
        _cartItems.update { currentCart ->
            val existingItem = currentCart.find { it.product.id == product.id }

            if (existingItem != null) {
                // Product already in cart, increment quantity
                currentCart.map { cartItem ->
                    if (cartItem.product.id == product.id) {
                        cartItem.copy(quantity = cartItem.quantity + 1)
                    } else {
                        cartItem
                    }
                }
            } else {
                // New product, add to cart with quantity 1
                currentCart + CartItem(product, 1)
            }
        }
    }

    /**
     * Remove a product from cart or decrement quantity
     */
    override suspend fun removeFromCart(product: Product) {
        _cartItems.update { currentCart ->
            val existingItem = currentCart.find { it.product.id == product.id }

            if (existingItem != null && existingItem.quantity > 1) {
                // Decrement quantity
                currentCart.map { cartItem ->
                    if (cartItem.product.id == product.id) {
                        cartItem.copy(quantity = cartItem.quantity - 1)
                    } else {
                        cartItem
                    }
                }
            } else {
                // Remove item completely
                currentCart.filter { it.product.id != product.id }
            }
        }
    }

    /**
     * Set specific quantity for a product
     */
    override suspend fun setQuantity(product: Product, quantity: Int) {
        if (quantity <= 0) {
            // Remove from cart if quantity is 0 or negative
            _cartItems.update { it.filter { item -> item.product.id != product.id } }
        } else {
            _cartItems.update { currentCart ->
                val existingItem = currentCart.find { it.product.id == product.id }

                if (existingItem != null) {
                    // Update quantity
                    currentCart.map { cartItem ->
                        if (cartItem.product.id == product.id) {
                            cartItem.copy(quantity = quantity)
                        } else {
                            cartItem
                        }
                    }
                } else {
                    // Add new item with specified quantity
                    currentCart + CartItem(product, quantity)
                }
            }
        }
    }

    /**
     * Get the quantity of a specific product in cart
     */
    override suspend fun getQuantity(productId: Int): Int {
        return _cartItems.value.find { it.product.id == productId }?.quantity ?: 0
    }

    /**
     * Clear the entire cart
     */
    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }
}