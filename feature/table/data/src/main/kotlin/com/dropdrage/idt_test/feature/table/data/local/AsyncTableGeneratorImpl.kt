package com.dropdrage.idt_test.feature.table.data.local

import com.dropdrage.idt_test.feature.table.domain.model.Cell
import com.dropdrage.idt_test.feature.table.domain.model.CellId
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Inject
@ContributesBinding(AppScope::class)
internal class AsyncTableGeneratorImpl : TableGenerator {

    private val sampleWords = listOf(
        "Alpha", "Beta", "Gamma", "Delta", "Echo", "Omega", "Zeta",
        "Data", "Table", "Tablet", "Compose", "Kotlin", "Matrix",
    )

    override suspend fun generateTable(rows: Int, columns: Int): List<List<Cell>> = withContext(Dispatchers.Default) {
        List(rows) { row ->
            List(columns) { column ->
                Cell(
                    cellId = CellId(row, column),
                    text = "${sampleWords.random()}-${(10..99).random()}",
                )
            }
        }
    }
}
