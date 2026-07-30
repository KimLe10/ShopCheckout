package com.kimle.shopcheckout.cart

import com.kimle.shopcheckout.data.Product

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val subtotalCents: Long get() = product.priceCents * quantity
}
