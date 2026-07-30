package com.kimle.shopcheckout.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kimle.shopcheckout.cart.CartViewModel
import com.kimle.shopcheckout.ui.cart.CartScreen
import com.kimle.shopcheckout.ui.catalog.CatalogScreen
import com.kimle.shopcheckout.ui.checkout.CheckoutScreen

private object Routes {
    const val CATALOG = "catalog"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
}

@Composable
fun ShopNavGraph(navController: NavHostController = rememberNavController()) {
    // Shared across the whole graph so the cart survives navigation between screens.
    val cartViewModel: CartViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.CATALOG) {
        composable(Routes.CATALOG) {
            CatalogScreen(
                cartViewModel = cartViewModel,
                onNavigateToCart = { navController.navigate(Routes.CART) }
            )
        }
        composable(Routes.CART) {
            CartScreen(
                cartViewModel = cartViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate(Routes.CHECKOUT) }
            )
        }
        composable(Routes.CHECKOUT) {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
