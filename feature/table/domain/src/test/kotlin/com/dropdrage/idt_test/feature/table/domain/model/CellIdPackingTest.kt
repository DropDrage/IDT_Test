package com.dropdrage.idt_test.feature.table.domain.model

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

internal class CellIdPackingTest : ShouldSpec(
    {
        context("CellId construct with row and column") {
            withData(
                listOf(
                    3 to 4,
                    1000 to 6,
                    Int.MAX_VALUE to 1,
                    3 to Int.MAX_VALUE,
                    Int.MAX_VALUE to Int.MAX_VALUE,
                ),
            ) { (row, column) ->
                val cellId = CellId(row, column)
                should("return same row and column as passed into constructor") {
                    cellId.row shouldBe row
                    cellId.column shouldBe column
                }
            }
        }
    },
)
