package com.example.exposed.dsl

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OrderTable : Table("orders") {
    val id = integer("id").autoIncrement()
    val created = timestamp("created")

    override val primaryKey = PrimaryKey(id)
}
