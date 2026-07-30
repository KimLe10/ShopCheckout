package com.kimle.shopcheckout.checkout

import android.content.Context
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

/**
 * Builds the JSON request objects for the Google Pay API following Google's documented
 * quickstart pattern (https://developers.google.com/pay/api/android/guides/tutorial).
 * Runs entirely against WalletConstants.ENVIRONMENT_TEST — no real merchant account,
 * card processor, or money is involved.
 */
object PaymentsUtil {

    fun createPaymentsClient(context: Context): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()
        return Wallet.getPaymentsClient(context, walletOptions)
    }

    private fun baseRequest() = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    private fun allowedCardNetworks() =
        JSONArray(listOf("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA"))

    private fun allowedCardAuthMethods() =
        JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS"))

    private fun baseCardPaymentMethod(): JSONObject = JSONObject().apply {
        put("type", "CARD")
        put(
            "parameters",
            JSONObject().apply {
                put("allowedAuthMethods", allowedCardAuthMethods())
                put("allowedCardNetworks", allowedCardNetworks())
                put("billingAddressRequired", false)
            }
        )
    }

    private fun gatewayTokenizationSpecification(): JSONObject = JSONObject().apply {
        put("type", "PAYMENT_GATEWAY")
        put(
            "parameters",
            JSONObject().apply {
                // "example" is Google's own placeholder test gateway - safe with ENVIRONMENT_TEST,
                // used the same way in Google's official sample apps.
                put("gateway", "example")
                put("gatewayMerchantId", "exampleGatewayMerchantId")
            }
        )
    }

    private fun cardPaymentMethod(): JSONObject =
        baseCardPaymentMethod().apply {
            put("tokenizationSpecification", gatewayTokenizationSpecification())
        }

    fun isReadyToPayRequest(): JSONObject? = try {
        baseRequest().apply {
            put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod()))
        }
    } catch (e: JSONException) {
        null
    }

    fun getPaymentDataRequest(priceCents: Long, merchantName: String): JSONObject? = try {
        baseRequest().apply {
            put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod()))
            put("transactionInfo", transactionInfo(priceCents))
            put(
                "merchantInfo",
                JSONObject().apply { put("merchantName", merchantName) }
            )
            put("shippingAddressRequired", false)
            put("emailRequired", false)
        }
    } catch (e: JSONException) {
        null
    }

    private fun transactionInfo(priceCents: Long): JSONObject {
        val price = priceCents / 100.0
        return JSONObject().apply {
            put("totalPrice", String.format(Locale.US, "%.2f", price))
            put("totalPriceStatus", "FINAL")
            put("countryCode", "US")
            put("currencyCode", "USD")
        }
    }
}
