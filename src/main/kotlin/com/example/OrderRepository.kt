package com.example

import java.time.Instant

interface OrderRepository {
    fun save(order: Order): Order

    fun findById(id: Int): Order?

    fun findByIdWithOrderLines(orderId: Int): Order?
}

data class Order(
    val id: Int = 0,
    val created: Instant,
    val orderLines: List<OrderLine>,
)

data class OrderLine(
    val price: Double,
)
