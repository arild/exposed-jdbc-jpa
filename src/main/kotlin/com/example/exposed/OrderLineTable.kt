package com.example.exposed

import org.jetbrains.exposed.sql.Table

object OrderLineTable : Table("order_line") {
    val id = long("id")
    val orderId = long("order_id")
        .uniqueIndex()
        .references(OrderTable.id)
    val price = decimal("price", precision = 2, scale = 2)

}
