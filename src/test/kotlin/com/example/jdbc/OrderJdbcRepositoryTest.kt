package com.example.jdbc

import com.example.Order
import com.example.OrderLine
import com.example.config.DatabaseTest
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class OrderJdbcRepositoryTest(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : DatabaseTest({
    val orderRepository = OrderJdbcRepository(jdbcTemplate)

    "Should store order" {
        val order = Order(
            created = Instant.now(),
            orderLines = listOf(
                OrderLine(price = 10.0), OrderLine(price = 20.0)
            ),
        )

        orderRepository.save(order) shouldBe order.copy(id = 1)
        orderRepository.save(order) shouldBe order.copy(id = 2)
    }

    "Should find order without order lines" {
        val order = Order(
            created = Instant.now(),
            orderLines = listOf(OrderLine(price = 10.0), OrderLine(price = 20.0)),
        )
        orderRepository.save(order)

        orderRepository.findById(1) shouldBe order.copy(id = 1, orderLines = emptyList())
        orderRepository.findById(2).shouldBeNull()
    }

    "Should find order with order lines" {
        val order = Order(
            created = Instant.now(),
            orderLines = listOf(OrderLine(price = 10.0), OrderLine(price = 20.0)),
        )
        orderRepository.save(order)
        orderRepository.save(order)

        orderRepository.findByIdWithOrderLines(1) shouldBe order.copy(id = 1)
        orderRepository.findByIdWithOrderLines(2) shouldBe order.copy(id = 2)
        orderRepository.findByIdWithOrderLines(3).shouldBeNull()
    }
})
