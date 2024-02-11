package com.example.jdbc

import java.math.BigDecimal

data class OrderLine(
    val id: Long = 0,
    val price: BigDecimal,
)
