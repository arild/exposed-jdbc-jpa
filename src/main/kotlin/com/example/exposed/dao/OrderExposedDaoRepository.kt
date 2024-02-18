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
        return order.copy(
            id = savedOrder.id.value,
            orderLines = savedOrderLines.map { orderLine ->
                OrderLine(
                    price = orderLine.price
                )
            }
        )
    }

    @Transactional(readOnly = true)
    override fun findById(id: Int): Order? {
        return OrderEntity
            .findById(id)
            ?.let {
                Order(
                    id = it.id.value,
                    created = it.created,
                    orderLines = emptyList(),
                )
            }
    }

    @Transactional(readOnly = true)
    override fun findByIdWithOrderLines(orderId: Int): Order? {
        val query = Orders
            .innerJoin(OrderLines)
            .selectAll()
            .where(Orders.id eq orderId)

        return OrderEntity
            .wrapRows(query)
            .map {
                Order(
                    id = it.id.value,
                    created = it.created,
                    orderLines = it.orderLines.map { orderLine ->
                        OrderLine(
                            price = orderLine.price
                        )
                    }
                )
            }
            .firstOrNull()
    }
}
