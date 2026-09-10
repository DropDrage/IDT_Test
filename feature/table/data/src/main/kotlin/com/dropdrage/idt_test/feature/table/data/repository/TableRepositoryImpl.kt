package com.dropdrage.idt_test.feature.table.data.repository

import com.dropdrage.idt_test.feature.table.data.local.TableGenerator
import com.dropdrage.idt_test.feature.table.domain.model.Cell
import com.dropdrage.idt_test.feature.table.domain.repository.TableRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
internal class TableRepositoryImpl(
    private val tableGenerator: TableGenerator,
) : TableRepository {

    override suspend fun generateTable(rows: Int, columns: Int): List<List<Cell>> {
        return tableGenerator.generateTable(rows, columns)
    }
}
