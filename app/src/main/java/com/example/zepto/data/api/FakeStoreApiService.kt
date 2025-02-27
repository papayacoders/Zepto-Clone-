package com.example.zepto.data.api

import com.example.zepto.data.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Service class to fetch products from Fake Store API
 */
class FakeStoreApiService {
    private val baseUrl = "https://fakestoreapi.com"

    /**
     * Fetches all products from the Fake Store API
     */
    suspend fun fetchProducts(): List<Product> = withContext(Dispatchers.IO) {
        val url = URL("$baseUrl/products")
        val connection = url.openConnection() as HttpsURLConnection

        try {
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                parseProductsResponse(response)
            } else {
                emptyList()
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Fetches products by category
     */
    suspend fun fetchProductsByCategory(category: String): List<Product> = withContext(Dispatchers.IO) {
        val url = URL("$baseUrl/products/category/$category")
        val connection = url.openConnection() as HttpsURLConnection

        try {
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                parseProductsResponse(response)
            } else {
                emptyList()
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Fetches available categories from the API
     */
    suspend fun fetchCategories(): List<String> = withContext(Dispatchers.IO) {
        val url = URL("$baseUrl/products/categories")
        val connection = url.openConnection() as HttpsURLConnection

        try {
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                parseCategoriesResponse(response)
            } else {
                emptyList()
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Parses the JSON response for products
     */
    private fun parseProductsResponse(response: String): List<Product> {
        val products = mutableListOf<Product>()
        val jsonArray = JSONArray(response)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val title = jsonObject.getString("title")
            val price = jsonObject.getDouble("price")
            val imageUrl = jsonObject.getString("image")
            val category = jsonObject.getString("category")

            products.add(
                Product(
                    id = id,
                    name = title,
                    price = price,
                    imageUrl = imageUrl,
                    category = category
                )
            )
        }

        return products
    }

    /**
     * Parses the JSON response for categories
     */
    private fun parseCategoriesResponse(response: String): List<String> {
        val categories = mutableListOf<String>()
        val jsonArray = JSONArray(response)

        for (i in 0 until jsonArray.length()) {
            categories.add(jsonArray.getString(i))
        }

        return categories
    }
}