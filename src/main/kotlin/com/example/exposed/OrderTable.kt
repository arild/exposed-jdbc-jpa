package com.example.exposed

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OrderTable : Table("orders") {
    val id = long("id").autoIncrement()
    val created = timestamp("created")

    override val primaryKey = PrimaryKey(id)
}

object Orders : LongIdTable() {
    val name = timestamp("created")
}

class OrderEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<OrderEntity>(Orders)

    val created by Orders.name
}
