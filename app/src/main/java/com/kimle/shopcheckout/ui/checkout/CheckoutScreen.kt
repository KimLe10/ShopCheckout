package com.kimle.shopcheckout.ui.checkout

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.contract.ApiTaskResult
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.kimle.shopcheckout.cart.CartViewModel
import com.kimle.shopcheckout.checkout.CheckoutViewModel
import com.kimle.shopcheckout.data.formatCentsAsUsd
import com.kimle.shopcheckout.ui.components.PrimaryButton

private const val MERCHANT_NAME = "ShopCheckout Demo"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit,
    checkoutViewModel: CheckoutViewModel = viewModel()
) {
    val cartItems by cartViewModel.items.collectAsStateWithLifecycle()
    val uiState by checkoutViewModel.uiState.collectAsStateWithLifecycle()
    val totalCents = cartViewModel.totalCents

    val paymentDataLauncher = rememberLauncherForActivityResult(
        contract = TaskResultContracts.GetPaymentDataResult()
    ) { taskResult: ApiTaskResult<PaymentData> ->
        checkoutViewModel.onPaymentResult(taskResult)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems, key = { it.product.id }) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.product.name} x${item.quantity}")
                        Text(item.subtotalCents.formatCentsAsUsd())
                    }
                }
                item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total")
                        Text(totalCents.formatCentsAsUsd())
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                when {
                    uiState.lastFourDigits != null -> {
                        Text("Payment authorized — card ending in ${uiState.lastFourDigits}")
                        Text("This ran against Google Pay's TEST environment; no real charge occurred.")
                    }
                    uiState.checkingReadiness -> {
                        CircularProgressIndicator()
                    }
                    !uiState.googlePayAvailable -> {
                        Text("Google Pay isn't available on this device/emulator.")
                    }
                    else -> {
                        uiState.errorMessage?.let { Text(it) }
                        PrimaryButton(
                            text = if (uiState.paymentInProgress) "Processing…" else "Pay with Google Pay",
                            enabled = !uiState.paymentInProgress && cartItems.isNotEmpty(),
                            onClick = {
                                val task = checkoutViewModel.buildPaymentDataTask(totalCents, MERCHANT_NAME)
                                if (task != null) {
                                    paymentDataLauncher.launch(task)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
