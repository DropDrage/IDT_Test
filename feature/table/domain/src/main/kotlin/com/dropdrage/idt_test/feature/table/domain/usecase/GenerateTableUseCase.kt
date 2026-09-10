package com.dropdrage.idt_test.feature.table.domain.usecase

import com.dropdrage.idt_test.feature.table.domain.model.Cell
import com.dropdrage.idt_test.feature.table.domain.repository.TableRepository
import dev.zacsweers.metro.Inject

@Inject
class GenerateTableUseCase(private val repository: TableRepository) {
    suspend operator fun invoke(rows: Int, cols: Int): List<List<Cell>> {
        return repository.generateTable(rows, cols)
    }
}
