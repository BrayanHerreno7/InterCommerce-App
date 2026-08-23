package com.ingenia.intercommerce.di

import android.app.Application
import androidx.room.Room
import com.ingenia.intercommerce.core.data.local.InterCommerceDatabase
import com.ingenia.intercommerce.core.data.remote.DummyJsonApi
import com.ingenia.intercommerce.feature.cart.data.CartRepositoryImpl
import com.ingenia.intercommerce.feature.cart.data.local.CartDao
import com.ingenia.intercommerce.feature.cart.domain.CartRepository
import com.ingenia.intercommerce.feature.catalog.data.ProductRepositoryImpl
import com.ingenia.intercommerce.feature.catalog.data.local.ProductDao
import com.ingenia.intercommerce.feature.catalog.domain.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): InterCommerceDatabase {
        return Room.databaseBuilder(
            app,
            InterCommerceDatabase::class.java,
            "intercommerce_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCartDao(db: InterCommerceDatabase): CartDao {
        return db.cartDao
    }

    @Provides
    @Singleton
    fun provideProductDao(db: InterCommerceDatabase): ProductDao {
        return db.productDao
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepositoryImpl(cartDao)
    }

    @Provides
    @Singleton
    fun provideProductRepository(api: DummyJsonApi, productDao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(api, productDao)
    }
}
