package com.example.jdbc

import com.example.config.DatabaseTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class OrderRepositoryTest(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : DatabaseTest({
    val orderRepository = OrderRepository(jdbcTemplate)

    "Should store and find order" {
        val created = Instant.now()
        val order = Order(
            created = created,
            orderLines = emptyList(),
        )
        val saved = orderRepository.save(order)

        val result = orderRepository.findByIdOrNull(saved.id)
    }
})
