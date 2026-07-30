package com.kimle.shopcheckout.checkout

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class PaymentsUtilTest {

    @Test
    fun `isReadyToPayRequest declares CARD as an allowed payment method`() {
        val request = PaymentsUtil.isReadyToPayRequest()

        assertNotNull(request)
        val allowedMethods = request!!.getJSONArray("allowedPaymentMethods")
        assertEquals(1, allowedMethods.length())
        assertEquals("CARD", allowedMethods.getJSONObject(0).getString("type"))
    }

    @Test
    fun `getPaymentDataRequest formats cents into a decimal total price`() {
        val request = PaymentsUtil.getPaymentDataRequest(priceCents = 12345, merchantName = "Test Merchant")

        assertNotNull(request)
        val transactionInfo = request!!.getJSONObject("transactionInfo")
        assertEquals("123.45", transactionInfo.getString("totalPrice"))
        assertEquals("USD", transactionInfo.getString("currencyCode"))
    }

    @Test
    fun `getPaymentDataRequest includes the supplied merchant name`() {
        val request = PaymentsUtil.getPaymentDataRequest(priceCents = 999, merchantName = "ShopCheckout Demo")

        val merchantInfo = request!!.getJSONObject("merchantInfo")
        assertEquals("ShopCheckout Demo", merchantInfo.getString("merchantName"))
    }
}
