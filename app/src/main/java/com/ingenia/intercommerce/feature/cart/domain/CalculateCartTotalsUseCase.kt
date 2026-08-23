package com.ingenia.intercommerce.feature.cart.domain

import javax.inject.Inject

class CalculateCartTotalsUseCase @Inject constructor() {
    operator fun invoke(items: List<CartItem>): CartTotals {
        val subtotal = items.sumOf { it.price * it.quantity }
        val taxRate = 0.19 // 19% IVA (Ejemplo Colombia)
        val tax = subtotal * taxRate
        val total = subtotal + tax
        return CartTotals(
            subtotal = subtotal,
            tax = tax,
            total = total
        )
    }
}
