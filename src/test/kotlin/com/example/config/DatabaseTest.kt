package com.example.config

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.Database
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import javax.sql.DataSource

@DataJpaTest
@ContextConfiguration(initializers = [PostgresContainer::class])
@Import(ExposedConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DatabaseTest(body: StringSpec.() -> Unit = {}) : StringSpec(body) {

    override suspend fun beforeEach(testCase: TestCase) = truncateTables()
}

@Configuration
class ExposedConfiguration(
    dataSource: DataSource,
) {
    init {
        Database.connect(dataSource)
    }

    @Bean
    fun transactionManager(dataSource: DataSource): SpringTransactionManager = SpringTransactionManager(dataSource)
}
