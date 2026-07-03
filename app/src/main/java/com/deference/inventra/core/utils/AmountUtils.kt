package com.deference.inventra.core.utils

const val CURRENCY_SYMBOL = "₹"

fun Double.asAmount(decimals: Int = 2) = "${CURRENCY_SYMBOL}%.${decimals}f".format(this)