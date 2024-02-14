package com.example.exposed

import com.example.Order
import com.example.OrderRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderExposedRepository : OrderRepository {

    @Transactional
    override fun save(order: Order): Order {
        return OrderTable.insert {
            it[created] = order.created
        }.resultedValues!!
            .first()
            .toOrder()
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Order? {
        return OrderTable
            .selectAll()
            .where { OrderTable.id eq id }
            .firstOrNull()
            ?.toOrder()
    }

    private fun ResultRow.toOrder() = Order(
        id = this[OrderTable.id],
        created = this[OrderTable.created],
        orderLines = emptyList(),
    )
}
