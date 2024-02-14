package com.example.exposed

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp


object OrderTable : Table("orders") {
    val id = long("id").autoIncrement()
    val created = timestamp("created")

    override val primaryKey = PrimaryKey(id)
}
