package com.example.jpa

import com.example.config.DatabaseTest
import io.kotest.matchers.shouldBe
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.time.Instant

class OrderRepositoryTest(val orderRepository: OrderRepository) : DatabaseTest({

    "Should store and find order" {
        val order = Order(
            id = 1,
            created = Instant.now(),
            listOf(OrderLine(price = BigDecimal(10.0)), OrderLine(price = BigDecimal(10.0))),
        )
        val saved = orderRepository.save(order)

        val result = orderRepository.findByIdOrNull(saved.id)

        result?.orderLines?.size shouldBe 2
    }
})
