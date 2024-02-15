package com.example.exposed

import com.example.Order
import com.example.OrderLine
import com.example.config.DatabaseTest
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Instant

class OrderExposedRepositoryTest : DatabaseTest({
    val orderRepository = OrderExposedRepository(OrderLineRepository())

    "Should store order" {
        val order = Order(
            created = Instant.now(),
            orderLines = listOf(
                OrderLine(price = 10.0), OrderLine(price = 20.0)
            ),
        )

        orderRepository.save(order) shouldBe order.copy(id = 1L)
        orderRepository.save(order) shouldBe order.copy(id = 2L)
    }

    "Should find order without order lines" {
        val order = Order(
            created = Instant.now(),
            orderLines = listOf(OrderLine(price = 10.0), OrderLine(price = 20.0)),
        )
        orderRepository.save(order)

        orderRepository.findById(1L) shouldBe order.copy(id = 1L, orderLines = emptyList())
        orderRepository.findById(2L).shouldBeNull()
    }

    "Should find order with order lines" {
        val order = Order(
            created = Instant.now(),
            orderLines = listOf(OrderLine(price = 10.0), OrderLine(price = 20.0)),
        )
        orderRepository.save(order)
        orderRepository.save(order)

        orderRepository.findByIdWithOrderLines(1L) shouldBe order.copy(id = 1L)
        orderRepository.findByIdWithOrderLines(2L) shouldBe order.copy(id = 2L)
        orderRepository.findByIdWithOrderLines(3L).shouldBeNull()
    }
})