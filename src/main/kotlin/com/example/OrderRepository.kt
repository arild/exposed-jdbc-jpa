package com.example

import com.example.jpa.OrderLine
import java.math.BigDecimal
import java.time.Instant

interface OrderRepository {
    fun save(order: Order): Order

    fun findById(id: Long): Order?
}

data class Order(
    val id: Long = 0,
    val created: Instant,
    val orderLines: List<OrderLine>,
)

data class OrderLine(
    val id: Long,
    val price: BigDecimal,
)
