package com.example.zepto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zepto.data.models.CartItem
import com.example.zepto.data.models.Product
import com.example.zepto.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    // StateFlow to hold the current cart items
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Total items in cart - make this mutable internally
    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems

    // Total price of items in cart - make this mutable internally
    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice

    // Initialize by collecting data from repository
    init {
        viewModelScope.launch {
            // Collect cart items from repository
            cartRepository.getCartItems().collect { items ->
                _cartItems.value = items
                updateCartTotals()
            }
        }
    }

    /**
     * Add a product to cart or increment its quantity if already present
     */
    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product)
        }
    }

    /**
     * Remove a product from cart or decrement quantity
     */
    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            cartRepository.removeFromCart(product)
        }
    }

    /**
     * Set specific quantity for a product
     */
    fun setQuantity(product: Product, quantity: Int) {
        viewModelScope.launch {
            cartRepository.setQuantity(product, quantity)
        }
    }

    /**
     * Get the quantity of a specific product in cart
     */
    fun getQuantity(productId: Int): Int {
        // For immediate access, use the locally cached value
        return _cartItems.value.find { it.product.id == productId }?.quantity ?: 0
    }

    /**
     * Clear the entire cart
     */
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    /**
     * Update cart totals (item count and price)
     */
    private fun updateCartTotals() {
        val items = _cartItems.value
        val totalItemsCount = items.sumOf { it.quantity }
        val totalPriceSum = items.sumOf { it.quantity * it.product.price }

        // Update the mutable state flows
        _totalItems.value = totalItemsCount
        _totalPrice.value = totalPriceSum
    }
}