package com.ingenia.intercommerce.feature.productdetail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingenia.intercommerce.feature.cart.domain.AddProductToCartUseCase
import com.ingenia.intercommerce.feature.cart.domain.CartItem
import com.ingenia.intercommerce.feature.catalog.domain.Product
import com.ingenia.intercommerce.feature.productdetail.domain.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle["productId"])

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _product.value = getProductDetailUseCase(productId)
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            addProductToCartUseCase(
                CartItem(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    imageUrl = product.thumbnail,
                    quantity = 1
                )
            )
        }
    }
}

