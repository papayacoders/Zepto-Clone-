package com.example.zepto.data.repository

import com.example.zepto.data.models.Category
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Category Repository
 */
interface CategoryRepository {
    /**
     * Gets all categories
     */
    fun getCategories(): Flow<List<Category>>

    /**
     * Gets a category by its ID or name
     */
    suspend fun getCategoryByIdOrName(idOrName: String): Category?
}