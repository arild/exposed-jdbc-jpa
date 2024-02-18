package com.example.jdbc

import com.example.Order
import com.example.OrderLine
import com.example.OrderRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types

@Repository
class OrderJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : OrderRepository {

    override fun save(order: Order): Order {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            "INSERT INTO orders(created) VALUES (:created)",
            MapSqlParameterSource()
                .addValue("created", Timestamp.from(order.created), Types.TIMESTAMP),
            keyHolder,
        )
        val orderId = keyHolder.keys?.get("id") as Int

        order.orderLines.forEach {
            jdbcTemplate.update(
                "INSERT INTO order_line(order_id, price) VALUES (:order_id, :price)",
                MapSqlParameterSource()
                    .addValue("order_id", orderId, Types.INTEGER)
                    .addValue("price", it.price, Types.DOUBLE),
            )
        }

        return Order(
            id = orderId,
            created = order.created,
            orderLines = order.orderLines
        )
    }

    override fun findById(id: Int): Order? {
        return jdbcTemplate.query(
            "SELECT * FROM orders WHERE id = :id",
            MapSqlParameterSource().addValue("id", id),
        ) { resultSet, _ ->
            resultSet.toOrder(orderLines = emptyList())
        }.firstOrNull()
    }

    override fun findByIdWithOrderLines(orderId: Int): Order? {
        return jdbcTemplate.query(
            """
            SELECT o.id, o.created, ol.price, ol.order_id FROM orders o
            INNER JOIN order_line ol on o.id = ol.order_id
            WHERE o.id = :orderId;
            """,
            MapSqlParameterSource().addValue("orderId", orderId),
        ) { resultSet, _ ->
            val res = resultSet.toOrdersWithOrderLines()
            res.firstOrNull()
        }.firstOrNull()
    }

    private fun ResultSet.toOrder(orderLines: List<OrderLine>) = Order(
        id = this.getInt("id"),
        created = this.getTimestamp("created").toInstant(),
        orderLines = orderLines,
    )

    private fun ResultSet.toOrderLine() = OrderLine(
        price = this.getDouble("price")
    )

    private fun ResultSet.toOrdersWithOrderLines(): List<Order> {
        val result = mutableMapOf<Int, Order>()
        do {
            val orderLine = this.toOrderLine()
            val order = this.toOrder(orderLines = emptyList())

            result.getOrPut(order.id) { order }.let {
                result.put(order.id, it.copy(orderLines = it.orderLines + orderLine))
            }
        } while (this.next())
        return result.values.toList()
    }
}
