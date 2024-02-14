package com.example

import java.time.Instant

data class Order(
    val id: Long = 0,
    val created: Instant,
    val orderLines: List<OrderLine>,
)
