package com.example.exposed.dao

import com.example.Order
import com.example.OrderLine
import com.example.OrderRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderExposedDaoRepository : OrderRepository {

    @Transactional
    override fun save(order: Order): Order {
        val savedOrder = OrderEntity.new {
            created = order.created
        }
        val savedOrderLines = order.orderLines.map {
            OrderLineEntity.new {
                price = it.price
                this.order = savedOrder
            }
        }
        return savedOrder.toOrder(savedOrderLines)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Int): Order? {
        return OrderEntity
            .findById(id)
            ?.toOrder(orderLines = emptyList())
    }

    @Transactional(readOnly = true)
    override fun findByIdWithOrderLines(orderId: Int): Order? {
        val query = Orders
            .innerJoin(OrderLines)
            .selectAll()
            .where(Orders.id eq orderId)

        return OrderEntity
            .wrapRows(query)
            .map { it.toOrder(it.orderLines.toList()) }
            .firstOrNull()
    }

    private fun OrderEntity.toOrder(orderLines: List<OrderLineEntity>): Order =
        Order(
            id = id.value,
            created = created,
            orderLines = orderLines.map { it.toOrderLine() },
        )

    private fun OrderLineEntity.toOrderLine(): OrderLine =
        OrderLine(price = price)
}
