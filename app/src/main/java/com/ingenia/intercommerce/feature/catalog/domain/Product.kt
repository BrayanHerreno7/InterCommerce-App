package com.ingenia.intercommerce.feature.catalog.domain

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val brand: String
)
