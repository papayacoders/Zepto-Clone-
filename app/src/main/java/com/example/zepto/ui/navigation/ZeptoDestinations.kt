package com.example.zepto.ui.navigation

/**
 * Sealed class representing all destinations in the Zepto app
 */
sealed class ZeptoDestinations(val route: String) {
    /**
     * Home screen destination
     */
    data object Home : ZeptoDestinations("home")

    /**
     * Search screen destination
     */
    data object Search : ZeptoDestinations("search")

    /**
     * Cart screen destination
     */
    data object Cart : ZeptoDestinations("cart")

    /**
     * Account screen destination
     */
    data object Account : ZeptoDestinations("account")

    /**
     * Category detail screen with dynamic category ID parameter
     */
    data object CategoryDetail : ZeptoDestinations("category/{categoryId}") {
        /**
         * Create route with specific category ID
         */
        fun createRoute(categoryId: String): String {
            return "category/$categoryId"
        }
    }

    /**
     * Product detail screen with dynamic product ID parameter
     */
    data object ProductDetail : ZeptoDestinations("product/{productId}") {
        /**
         * Create route with specific product ID
         */
        fun createRoute(productId: Int): String {
            return "product/$productId"
        }
    }
}