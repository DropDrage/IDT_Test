package com.dropdrage.idt_test.feature.table.presentation.ui.screen.mapper

import com.dropdrage.idt_test.feature.table.domain.model.Cell
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.CellUi

internal fun Cell.toUi(): CellUi = CellUi(
    cellId = cellId,
    text = text,
)
