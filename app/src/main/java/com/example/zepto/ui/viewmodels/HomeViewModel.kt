package com.example.zepto.ui.viewmodels


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zepto.R
import com.example.zepto.data.models.Category
import com.example.zepto.data.models.Product
import com.example.zepto.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home Screen
 */
class HomeViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        fetchCategories()
        fetchAllProducts()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getCategories().collect { apiCategories ->
                    val mappedCategories = mapApiCategoriesToUiCategories(apiCategories)
                    _categories.value = mappedCategories
                }
            } catch (e: Exception) {
                _error.value = "Failed to load categories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun fetchAllProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getProducts().collect { apiProducts ->
                    _products.value = apiProducts
                    // Call fixProductImageUrls after setting products
                    fixProductImageUrls()
                }
            } catch (e: Exception) {
                _error.value = "Failed to load products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProductsByCategory(categoryName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getProductsByCategory(categoryName).collect { apiProducts ->
                    _products.value = apiProducts
                    // Call fixProductImageUrls after setting products
                    fixProductImageUrls()
                }
            } catch (e: Exception) {
                _error.value = "Failed to load products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
        if (category.name == "All") {
            fetchAllProducts()
        } else {
            fetchProductsByCategory(category.name.lowercase())
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
    fun fixProductImageUrls() {
        viewModelScope.launch {
            val existingProducts = _products.value
            val fixedProducts = existingProducts.map { product ->
                // Check if URL is valid or fix it
                val fixedImageUrl = when {
                    product.imageUrl.isEmpty() -> {
                        // No image URL, use empty string (will fall back to category image)
                        ""
                    }
                    !product.imageUrl.startsWith("http") && product.imageUrl.startsWith("/") -> {
                        // If URL is relative, prepend base URL
                        "https://fakestoreapi.com${product.imageUrl}"
                    }
                    !product.imageUrl.startsWith("http") -> {
                        // Malformed URL, prepend protocol
                        "https://${product.imageUrl}"
                    }
                    else -> {
                        // URL is already valid with protocol
                        product.imageUrl
                    }
                }

                // Return product with fixed URL
                product.copy(imageUrl = fixedImageUrl)
            }

            // Update the products flow
            _products.value = fixedProducts
        }
    }
}
