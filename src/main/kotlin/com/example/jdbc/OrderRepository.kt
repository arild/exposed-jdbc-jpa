package com.example.jdbc

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.sql.Types
import java.time.Instant

@Repository
@Component
class OrderRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun save(order: Order): Order {
        val params = MapSqlParameterSource()
            .addValue("orderId", order.id)
            .addValue("created", Timestamp.from(Instant.now()), Types.TIMESTAMP)
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            """
                INSERT INTO orders(id, created)
                VALUES (:orderId, :created)
            """,
            params,
            keyHolder,
        )
        return Order(
            id = keyHolder.keys?.get("id") as Long,
            created = order.created,
            orderLines = emptyList(),
        )
    }

    fun findByIdOrNull(orderId: Long): Order? {
        return jdbcTemplate.query(
            """
                SELECT * FROM orders
                WHERE id = :orderId
            """,
            MapSqlParameterSource().addValue("orderId", orderId),
        ) { rowMapper, _ ->
            Order(
                id = rowMapper.getLong("id"),
                created = rowMapper.getTimestamp("created").toInstant(),
                orderLines = emptyList(),
            )
        }.firstOrNull()
    }
}
