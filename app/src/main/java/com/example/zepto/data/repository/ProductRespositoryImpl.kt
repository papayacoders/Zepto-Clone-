package com.example.zepto.data.repository

import com.example.zepto.data.api.FakeStoreApiService
import com.example.zepto.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ProductRepository interface
 */
@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : ProductRepository() {

    /**
     * Gets all products from the API
     */
    override fun getProducts(): Flow<List<Product>> = flow {
        val products = apiService.fetchProducts()
        emit(products)
    }

    /**
     * Gets products by category
     */
    override fun getProductsByCategory(category: String): Flow<List<Product>> = flow {
        val products = apiService.fetchProductsByCategory(category)
        emit(products)
    }

    /**
     * Gets all available categories
     */
    override fun getCategories(): Flow<List<String>> = flow {
        val categories = apiService.fetchCategories()
        emit(categories)
    }
}