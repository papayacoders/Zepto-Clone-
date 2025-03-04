package com.example.zepto.data.repository

import com.example.zepto.data.api.FakeStoreApiService
import com.example.zepto.data.models.Product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository class to manage product data
 */
open class ProductRepository {
    private val apiService = FakeStoreApiService()

    /**
     * Gets all products from the API
     */
    open fun getProducts(): Flow<List<Product>> = flow {
        val products = apiService.fetchProducts()
        emit(products)
    }

    /**
     * Gets products by category
     */
    open fun getProductsByCategory(category: String): Flow<List<Product>> = flow {
        val products = apiService.fetchProductsByCategory(category)
        emit(products)
    }

    /**
     * Gets all available categories
     */
    open fun getCategories(): Flow<List<String>> = flow {
        val categories = apiService.fetchCategories()
        emit(categories)
    }
}