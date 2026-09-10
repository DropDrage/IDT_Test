package com.dropdrage.idt_test.feature.table.domain.usecase

import com.dropdrage.idt_test.feature.table.domain.model.Cell
import com.dropdrage.idt_test.feature.table.domain.model.CellId
import com.dropdrage.idt_test.feature.table.domain.repository.TableRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

internal class GenerateTableUseCaseTest : BehaviorSpec(
    {
        given("rows and columns count") {
            val rows = 3
            val columns = 4
            val repository = mockk<TableRepository> {
                coEvery { generateTable(eq(rows), eq(columns)) } returns List(rows) { row ->
                    List(columns) { column ->
                        Cell(CellId(row = row, column = column), text = "$row $column")
                    }
                }
            }
            val useCase = GenerateTableUseCase(repository)

            `when`("invoke") {
                val result = useCase.invoke(rows, columns)

                then("return same size Cell grid") {
                    coVerify(atMost = 1) { repository.generateTable(eq(rows), eq(columns)) }
                    result shouldHaveSize rows
                    result.forEach { it shouldHaveSize columns }
                }
            }
        }
    },
)
