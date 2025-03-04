package com.example.zepto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zepto.data.models.CategoryUiState
import com.example.zepto.data.models.Product
import com.example.zepto.data.repository.CategoryRepository
import com.example.zepto.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    // UI state as a StateFlow
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    /**
     * Load a category and its products by ID or name
     */
    fun loadCategory(categoryId: String) {
        viewModelScope.launch {
            // Start loading
            _uiState.update { it.copy(
                isLoading = true,
                error = null,
                categoryId = categoryId
            ) }

            try {
                // Try to find the category
                val category = categoryRepository.getCategoryByIdOrName(categoryId)

                if (category != null) {
                    // We found the category, now load its products
                    // Collect the first emission from the Flow to get the List<Product>
                    val products = productRepository.getProductsByCategory(category.name).first()

                    _uiState.update { it.copy(
                        categoryName = formatCategoryName(category.name),
                        products = products,
                        isLoading = false
                    ) }
                } else {
                    // We couldn't find the category
                    _uiState.update { it.copy(
                        categoryName = formatCategoryName(categoryId),
                        error = "Category not found",
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                // Handle error
                _uiState.update { it.copy(
                    error = "Error loading category: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }

    /**
     * Format a category name for display
     */
    private fun formatCategoryName(category: String): String {
        return when (category.lowercase()) {
            "electronics" -> "Electronics"
            "jewelery" -> "Jewelry"
            "men's clothing" -> "Men's Fashion"
            "women's clothing" -> "Women's Fashion"
            else -> category.split(" ")
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
        }
    }
}