package com.example.jdbc

import com.example.Order
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
            orderLines = emptyList(),
        )

        orderRepository.save(order) shouldBe order.copy(id = 1)
        orderRepository.save(order) shouldBe order.copy(id = 2)
    }

    "Should find order" {
        val order = Order(
            created = Instant.now(),
            orderLines = emptyList(),
        )
        orderRepository.save(order)

        orderRepository.findById(1) shouldBe order.copy(id = 1)
        orderRepository.findById(2).shouldBeNull()
    }
})
