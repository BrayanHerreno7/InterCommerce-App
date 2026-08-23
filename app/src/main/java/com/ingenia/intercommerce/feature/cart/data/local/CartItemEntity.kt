package com.ingenia.intercommerce.feature.cart.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingenia.intercommerce.feature.cart.domain.CartItem

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val productId: Int,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int
)

fun CartItemEntity.toDomain() = CartItem(
    productId = productId,
    title = title,
    price = price,
    imageUrl = imageUrl,
    quantity = quantity
)

fun CartItem.toEntity() = CartItemEntity(
    productId = productId,
    title = title,
    price = price,
    imageUrl = imageUrl,
    quantity = quantity
)
