package com.ingenia.intercommerce.core.data.remote

import com.ingenia.intercommerce.feature.catalog.data.remote.dto.ProductDto
import com.ingenia.intercommerce.feature.catalog.data.remote.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyJsonApi {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductResponseDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): ProductResponseDto

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductDto
}
