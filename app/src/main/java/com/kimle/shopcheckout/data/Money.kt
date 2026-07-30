package com.kimle.shopcheckout.data

import java.util.Locale

fun Long.formatCentsAsUsd(): String = String.format(Locale.US, "$%.2f", this / 100.0)
