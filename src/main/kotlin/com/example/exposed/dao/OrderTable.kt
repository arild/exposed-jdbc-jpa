package com.example.exposed.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object Orders : IntIdTable(name = "orders") {
    val created = timestamp("created")
}

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(Orders)

    var created by Orders.created
    val orderLines by OrderLineEntity referrersOn OrderLines.order
}

object OrderLines : IntIdTable(name = "order_line") {
    val price = double("price")
    val order = reference("order_id", Orders)
}

class OrderLineEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderLineEntity>(OrderLines)

    var price by OrderLines.price
    var order by OrderEntity referencedOn OrderLines.order
}
