package com.example.exposed

import com.example.Order
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderRepository {

    @Transactional
    fun save(order: Order): Order {
        return OrderTable.insert {
            it[created] = order.created
        }
            .resultedValues!!
            .first()
            .toOrder()
    }

    @Transactional(readOnly = true)
    fun findByIdOrNull(orderId: Long): Order? {
        return OrderTable
            .selectAll()
            .where { OrderTable.id eq orderId }
            .firstOrNull()
            ?.toOrder()
    }

    private fun ResultRow.toOrder() = Order(
        id = this[OrderTable.id],
        created = this[OrderTable.created],
        orderLines = emptyList(),
    )
}
