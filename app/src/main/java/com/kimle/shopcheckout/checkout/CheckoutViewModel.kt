package com.kimle.shopcheckout.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.contract.ApiTaskResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class CheckoutUiState(
    val checkingReadiness: Boolean = true,
    val googlePayAvailable: Boolean = false,
    val paymentInProgress: Boolean = false,
    val lastFourDigits: String? = null,
    val errorMessage: String? = null
)

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val paymentsClient: PaymentsClient = PaymentsUtil.createPaymentsClient(application)

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        checkGooglePayAvailability()
    }

    private fun checkGooglePayAvailability() {
        val request = PaymentsUtil.isReadyToPayRequest() ?: run {
            _uiState.value = _uiState.value.copy(
                checkingReadiness = false,
                googlePayAvailable = false,
                errorMessage = "Failed to build isReadyToPay request"
            )
            return
        }

        viewModelScope.launch {
            val ready = try {
                paymentsClient.isReadyToPay(IsReadyToPayRequest.fromJson(request.toString())).await()
            } catch (e: ApiException) {
                false
            }
            _uiState.value = _uiState.value.copy(
                checkingReadiness = false,
                googlePayAvailable = ready
            )
        }
    }

    /** Builds the loadPaymentData Task to hand off to the Activity Result launcher. */
    fun buildPaymentDataTask(totalCents: Long, merchantName: String): Task<PaymentData>? {
        val requestJson = PaymentsUtil.getPaymentDataRequest(totalCents, merchantName) ?: return null
        val request = PaymentDataRequest.fromJson(requestJson.toString())
        _uiState.value = _uiState.value.copy(paymentInProgress = true, errorMessage = null)
        return paymentsClient.loadPaymentData(request)
    }

    fun onPaymentResult(taskResult: ApiTaskResult<PaymentData>) {
        val paymentData = taskResult.result
        if (paymentData != null) {
            val info = paymentData.cardInfo
            _uiState.value = _uiState.value.copy(
                paymentInProgress = false,
                lastFourDigits = info?.cardDetails,
                errorMessage = null
            )
        } else {
            _uiState.value = _uiState.value.copy(
                paymentInProgress = false,
                errorMessage = taskResult.status.statusMessage ?: "Payment was not completed"
            )
        }
    }

    fun onPaymentCancelled() {
        _uiState.value = _uiState.value.copy(paymentInProgress = false)
    }

    fun resetPaymentResult() {
        _uiState.value = _uiState.value.copy(lastFourDigits = null, errorMessage = null)
    }
}
