package com.kimle.shopcheckout.cart

import com.kimle.shopcheckout.data.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    private lateinit var viewModel: CartViewModel

    private val earbuds = Product(id = "p1", name = "Earbuds", description = "", priceCents = 4999)
    private val keyboard = Product(id = "p2", name = "Keyboard", description = "", priceCents = 8999)

    @Before
    fun setUp() {
        viewModel = CartViewModel()
    }

    @Test
    fun `empty cart has zero total`() {
        assertTrue(viewModel.items.value.isEmpty())
        assertEquals(0L, viewModel.totalCents)
    }

    @Test
    fun `adding a product creates a cart item with quantity 1`() {
        viewModel.addProduct(earbuds)

        val items = viewModel.items.value
        assertEquals(1, items.size)
        assertEquals(earbuds, items.first().product)
        assertEquals(1, items.first().quantity)
        assertEquals(4999L, viewModel.totalCents)
    }

    @Test
    fun `adding the same product twice increments quantity instead of duplicating`() {
        viewModel.addProduct(earbuds)
        viewModel.addProduct(earbuds)

        val items = viewModel.items.value
        assertEquals(1, items.size)
        assertEquals(2, items.first().quantity)
        assertEquals(9998L, viewModel.totalCents)
    }

    @Test
    fun `total sums subtotals across distinct products`() {
        viewModel.addProduct(earbuds)
        viewModel.addProduct(keyboard)
        viewModel.addProduct(keyboard)

        assertEquals(4999L + 2 * 8999L, viewModel.totalCents)
    }

    @Test
    fun `decrementing below 1 removes the item from the cart`() {
        viewModel.addProduct(earbuds)

        viewModel.decrementQuantity(earbuds.id)

        assertTrue(viewModel.items.value.isEmpty())
        assertEquals(0L, viewModel.totalCents)
    }

    @Test
    fun `decrementing above 1 reduces quantity without removing the item`() {
        viewModel.addProduct(earbuds)
        viewModel.addProduct(earbuds)

        viewModel.decrementQuantity(earbuds.id)

        val items = viewModel.items.value
        assertEquals(1, items.size)
        assertEquals(1, items.first().quantity)
    }

    @Test
    fun `removeProduct removes the item regardless of quantity`() {
        viewModel.addProduct(earbuds)
        viewModel.addProduct(earbuds)

        viewModel.removeProduct(earbuds.id)

        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `clearCart empties all items`() {
        viewModel.addProduct(earbuds)
        viewModel.addProduct(keyboard)

        viewModel.clearCart()

        assertTrue(viewModel.items.value.isEmpty())
        assertEquals(0L, viewModel.totalCents)
    }

    @Test
    fun `decrementing a product not in the cart is a no-op`() {
        viewModel.decrementQuantity("not-in-cart")

        assertTrue(viewModel.items.value.isEmpty())
    }
}
