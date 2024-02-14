package com.example.jpa

import com.example.config.DatabaseTest
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.springframework.data.repository.findByIdOrNull
import java.time.Instant

class OrderRepositoryTest(val orderRepository: OrderRepository) : DatabaseTest({

    "Should store order" {
        val order1 = Order(
            created = Instant.now(),
            orderLines = emptyList(),
        )
        val order2 = Order(
            created = Instant.now(),
            orderLines = emptyList(),
        )

        orderRepository.save(order1) shouldBe order1.copy(id = 1L)
        orderRepository.save(order2) shouldBe order2.copy(id = 2L)
    }

    "Should find order" {
        val order = Order(
            created = Instant.now(),
            orderLines = emptyList(),
        )
        val saved = orderRepository.save(order)

        orderRepository.findByIdOrNull(saved.id) shouldBe order.copy(id = saved.id)
        orderRepository.findByIdOrNull(saved.id + 1).shouldBeNull()
    }
})
