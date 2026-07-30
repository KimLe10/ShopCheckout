package com.kimle.shopcheckout.cart

import androidx.lifecycle.ViewModel
import com.kimle.shopcheckout.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val totalCents: Long
        get() = _items.value.sumOf { it.subtotalCents }

    fun addProduct(product: Product) {
        val current = _items.value
        val existing = current.find { it.product.id == product.id }
        _items.value = if (existing != null) {
            current.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
            }
        } else {
            current + CartItem(product, quantity = 1)
        }
    }

    fun incrementQuantity(productId: String) {
        _items.value = _items.value.map {
            if (it.product.id == productId) it.copy(quantity = it.quantity + 1) else it
        }
    }

    fun decrementQuantity(productId: String) {
        val current = _items.value
        val existing = current.find { it.product.id == productId } ?: return
        _items.value = if (existing.quantity <= 1) {
            current.filterNot { it.product.id == productId }
        } else {
            current.map {
                if (it.product.id == productId) it.copy(quantity = it.quantity - 1) else it
            }
        }
    }

    fun removeProduct(productId: String) {
        _items.value = _items.value.filterNot { it.product.id == productId }
    }

    fun clearCart() {
        _items.value = emptyList()
    }
}
