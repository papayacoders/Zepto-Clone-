package com.example.zepto.data.repository

import com.example.zepto.R
import com.example.zepto.data.api.FakeStoreApiService
import com.example.zepto.data.models.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of CategoryRepository
 */
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : CategoryRepository {

    /**
     * Gets all categories mapped to UI model
     */
    override fun getCategories(): Flow<List<Category>> = flow {
        val apiCategories = apiService.fetchCategories()
        val mappedCategories = mapApiCategoriesToUiCategories(apiCategories)
        emit(mappedCategories)
    }

    /**
     * Gets a category by its ID or name
     */
    override suspend fun getCategoryByIdOrName(idOrName: String): Category? {
        val categories = mapApiCategoriesToUiCategories(apiService.fetchCategories())

        // Try to parse as ID first
        val id = idOrName.toIntOrNull()
        if (id != null) {
            return categories.find { it.id == id }
        }

        // If not an ID, try to match by name (case insensitive)
        return categories.find {
            it.name.equals(idOrName, ignoreCase = true) ||
                    it.name.replace(" ", "").equals(idOrName.replace(" ", ""), ignoreCase = true)
        }
    }

    /**
     * Maps API category names to UI Category objects with appropriate icons
     */
    private fun mapApiCategoriesToUiCategories(apiCategories: List<String>): List<Category> {
        val allCategory = Category(0, "All", R.drawable.shopping_bag_svgrepo_com)

        val mappedCategories = apiCategories.mapIndexed { index, categoryName ->
            val iconRes = when (categoryName.lowercase()) {
                "electronics" -> R.drawable.headphones_round_svgrepo_com
                "jewelery" -> R.drawable.wedding_relationships_couple_marry_svgrepo_com
                "men's clothing" -> R.drawable.faishon
                "women's clothing" -> R.drawable.beauty
                else -> R.drawable.shopping_bag_svgrepo_com
            }

            Category(index + 1, formatCategoryName(categoryName), iconRes)
        }

        return listOf(allCategory) + mappedCategories
    }

    /**
     * Formats category names for display (capitalizes first letter of each word)
     */
    private fun formatCategoryName(name: String): String {
        return name.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
    }
}