package com.dropdrage.idt_test.feature.table.presentation.ui.screen.model

import com.dropdrage.idt_test.feature.table.domain.model.CellId

internal sealed interface Command {
    class LoadData(val rows: Int, val columns: Int) : Command
    class ToggleCellColor(val cellId: CellId) : Command

    class EditCell(val cellId: CellId) : Command
    class ApplyCellEdit(val cellId: CellId, val text: String) : Command
    object CancelCellEdit : Command
}
