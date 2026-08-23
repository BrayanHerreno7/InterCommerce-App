package com.ingenia.intercommerce.navigation

import kotlinx.serialization.Serializable

@Serializable
object CatalogRoute

@Serializable
data class ProductDetailRoute(val productId: Int)

@Serializable
object CartRoute
