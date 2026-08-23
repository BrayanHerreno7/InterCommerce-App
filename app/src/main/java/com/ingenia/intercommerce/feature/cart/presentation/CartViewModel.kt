package com.ingenia.intercommerce.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingenia.intercommerce.feature.cart.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val calculateCartTotalsUseCase: CalculateCartTotalsUseCase,
    private val removeProductFromCartUseCase: CartRepository // direct call for brevity
) : ViewModel() {

    val cartItems = getCartUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartTotals = cartItems.map { items ->
        calculateCartTotalsUseCase(items)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartTotals(0.0, 0.0, 0.0)
    )

    fun removeItem(productId: Int) {
        viewModelScope.launch {
            removeProductFromCartUseCase.removeProduct(productId)
        }
    }
}
