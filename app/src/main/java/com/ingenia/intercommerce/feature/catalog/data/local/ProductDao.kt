package com.ingenia.intercommerce.feature.catalog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM products ORDER BY id ASC LIMIT :limit OFFSET :skip")
    suspend fun getProductsPaged(limit: Int, skip: Int): List<ProductEntity>
    
    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' COLLATE NOCASE")
    suspend fun searchProducts(query: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query("DELETE FROM products")
    suspend fun clearAll()
}
