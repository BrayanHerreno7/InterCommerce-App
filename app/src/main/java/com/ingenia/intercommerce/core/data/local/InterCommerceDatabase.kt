package com.ingenia.intercommerce.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ingenia.intercommerce.feature.cart.data.local.CartDao
import com.ingenia.intercommerce.feature.cart.data.local.CartItemEntity
import com.ingenia.intercommerce.feature.catalog.data.local.ProductDao
import com.ingenia.intercommerce.feature.catalog.data.local.ProductEntity

@Database(
    entities = [CartItemEntity::class, ProductEntity::class],
    version = 2,
    exportSchema = false
)
abstract class InterCommerceDatabase : RoomDatabase() {
    abstract val cartDao: CartDao
    abstract val productDao: ProductDao
}
