package com.kimle.shopcheckout.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kimle.shopcheckout.cart.CartViewModel
import com.kimle.shopcheckout.data.Product
import com.kimle.shopcheckout.data.ProductCatalog
import com.kimle.shopcheckout.data.formatCentsAsUsd
import com.kimle.shopcheckout.ui.components.CompactButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    cartViewModel: CartViewModel,
    onNavigateToCart: () -> Unit
) {
    val cartItems by cartViewModel.items.collectAsStateWithLifecycle()
    val itemCount = cartItems.sumOf { it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ShopCheckout") },
                actions = {
                    IconButton(onClick = onNavigateToCart) {
                        BadgedBox(badge = {
                            if (itemCount > 0) Badge { Text(itemCount.toString()) }
                        }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(ProductCatalog.products, key = { it.id }) { product ->
                ProductRow(product = product, onAddToCart = { cartViewModel.addProduct(product) })
            }
        }
    }
}

@Composable
private fun ProductRow(product: Product, onAddToCart: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(product.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(product.description, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(product.priceCents.formatCentsAsUsd())
            }
            CompactButton(text = "Add to Cart", onClick = onAddToCart)
        }
    }
}
