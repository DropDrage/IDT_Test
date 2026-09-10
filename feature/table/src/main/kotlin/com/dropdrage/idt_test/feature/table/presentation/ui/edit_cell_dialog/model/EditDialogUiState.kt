package com.dropdrage.idt_test.feature.table.presentation.ui.edit_cell_dialog.model

import com.dropdrage.idt_test.feature.table.domain.model.CellId

internal sealed interface EditDialogUiState {
    data class Visible(val cellId: CellId): EditDialogUiState
    data object Hidden : EditDialogUiState
}
