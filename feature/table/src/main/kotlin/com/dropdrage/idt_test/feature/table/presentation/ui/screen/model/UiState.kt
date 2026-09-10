package com.dropdrage.idt_test.feature.table.presentation.ui.screen.model

import com.dropdrage.idt_test.common.ui.utils.TextResource
import com.dropdrage.idt_test.feature.table.R
import com.dropdrage.idt_test.feature.table.domain.model.CellId
import com.dropdrage.idt_test.feature.table.presentation.ui.edit_cell_dialog.model.EditDialogUiState

internal typealias CellsGrid = List<List<CellUi>>

internal sealed interface UiState {
    val title: TextResource

    data class Data(
        override val title: TextResource,
        val cells: CellsGrid,
        val selectedCell: CellId? = null,
        val editDialogUiState: EditDialogUiState = EditDialogUiState.Hidden,
    ) : UiState

    data object Loading : UiState {
        override val title: TextResource = TextResource.id(R.string.table_title_loading)
    }
}
