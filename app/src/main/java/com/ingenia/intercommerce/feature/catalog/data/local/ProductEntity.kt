package com.ingenia.intercommerce.feature.catalog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingenia.intercommerce.feature.catalog.domain.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val brand: String,
    val page: Int // To keep track of pagination
)

fun ProductEntity.toDomain() = Product(
    id = id,
    title = title,
    description = description,
    price = price,
    thumbnail = thumbnail,
    brand = brand
)
