package com.example.zepto.di


import com.example.zepto.data.api.FakeStoreApiService
import com.example.zepto.data.repository.CartRepository
import com.example.zepto.data.repository.CartRepositoryImpl
import com.example.zepto.data.repository.CategoryRepository
import com.example.zepto.data.repository.CategoryRepositoryImpl
import com.example.zepto.data.repository.ProductRepository
import com.example.zepto.data.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides repository implementations
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    companion object {
        @Provides
        @Singleton
        fun provideFakeStoreApiService(): FakeStoreApiService {
            return FakeStoreApiService()
        }
    }
}