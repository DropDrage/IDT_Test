package com.dropdrage.idt_test.feature.table.data.local

import com.dropdrage.idt_test.feature.table.domain.model.Cell

interface TableGenerator {
    suspend fun generateTable(rows: Int, columns: Int): List<List<Cell>>
}
