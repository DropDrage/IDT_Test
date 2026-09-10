package com.dropdrage.idt_test.feature.table.domain.repository

import com.dropdrage.idt_test.feature.table.domain.model.Cell

interface TableRepository{
    suspend fun generateTable(rows: Int, columns: Int): List<List<Cell>>
}
