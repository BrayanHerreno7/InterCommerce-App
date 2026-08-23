package com.ingenia.intercommerce.feature.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ingenia.intercommerce.feature.catalog.domain.GetProductsUseCase
import com.ingenia.intercommerce.feature.catalog.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    
    val productsPagingFlow: Flow<PagingData<Product>> = getProductsUseCase()
        .cachedIn(viewModelScope)
}
