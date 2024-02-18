package com.example.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrderJpaRepository : JpaRepository<Order, Int> {

    @Query("""
        select o from Order o
        inner join fetch o.orderLines
        where o.id = :orderId
    """)
    fun findByIdWithOrderLines(orderId: Int): Order?
}
