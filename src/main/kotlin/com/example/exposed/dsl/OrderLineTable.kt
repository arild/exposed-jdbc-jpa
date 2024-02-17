package com.example.exposed.dsl

import org.jetbrains.exposed.sql.Table

object OrderLineTable : Table("order_line") {
    val orderId = integer("order_id")
        .uniqueIndex()
        .references(OrderTable.id)
    val price = double("price")
}
