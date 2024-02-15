package com.example.exposed

import com.example.Order
import com.example.OrderLine
import com.example.OrderRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderExposedRepository(val orderLineRepository: OrderLineRepository) : OrderRepository {

    @Transactional
    override fun save(order: Order): Order {
        val savedOrder = OrderTable.insert {
            it[created] = order.created
        }.resultedValues!!
            .first()
            .toOrder()

        return savedOrder.copy(
            orderLines = orderLineRepository.saveOrderLines(savedOrder.id, order.orderLines)
        )
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Order? {
        return OrderTable
            .selectAll()
            .where { OrderTable.id eq id }
            .firstOrNull()
            ?.toOrder()
    }

    @Transactional(readOnly = true)
    fun findByIdWithOrderLines(orderId: Long): Order? {
        return OrderTable
            .innerJoin(OrderLineTable)
            .selectAll()
            .where { OrderTable.id eq orderId }
            .toOrderWithOrderLines()
            .firstOrNull()
    }

    private fun ResultRow.toOrder() = Order(
        id = this[OrderTable.id],
        created = this[OrderTable.created],
        orderLines = emptyList(),
    )

    private fun Iterable<ResultRow>.toOrderWithOrderLines(): List<Order> =
        fold(mutableMapOf<Long, Order>()) { acc, resultRow ->
            val order = resultRow.toOrder()
            val orderLine = resultRow.getOrNull(OrderLineTable.orderId)
                ?.let { resultRow.toOrderLine() }

            val currentOrder = acc.getOrDefault(order.id, order)
            acc[order.id] = currentOrder.copy(orderLines = currentOrder.orderLines + listOfNotNull(orderLine))
            acc
        }.values.toList()
}

@Repository
class OrderLineRepository {

    @Transactional
    fun saveOrderLines(orderId: Long, orderLines: List<OrderLine>): List<OrderLine> {
        return OrderLineTable.batchInsert(orderLines) { orderLine ->
            this[OrderLineTable.orderId] = orderId
            this[OrderLineTable.price] = orderLine.price
        }.map { it.toOrderLine() }
    }
}

private fun ResultRow.toOrderLine() = OrderLine(
    price = this[OrderLineTable.price],
)
