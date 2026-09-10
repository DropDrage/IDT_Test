package com.dropdrage.idt_test.feature.table.presentation.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.idt_test.common.ui.utils.TextResource
import com.dropdrage.idt_test.feature.table.R
import com.dropdrage.idt_test.feature.table.domain.model.Cell
import com.dropdrage.idt_test.feature.table.domain.usecase.GenerateTableUseCase
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.Command
import com.dropdrage.idt_test.feature.table.presentation.ui.edit_cell_dialog.model.EditDialogUiState
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.UiState
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.mapper.toUi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class TableViewModel(
    private val generateTableUseCase: GenerateTableUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    internal val uiState = _uiState.asStateFlow()

    internal fun command(command: Command) {
        when (command) {
            is Command.LoadData -> loadData(command)
            else -> _uiState.update { state ->
                if (state !is UiState.Data) return@update state
                when (command) {
                    is Command.ToggleCellColor -> toggleCellColor(state, command)
                    is Command.EditCell -> editCell(state, command)
                    is Command.ApplyCellEdit -> updateCellText(state, command)
                    Command.CancelCellEdit -> cancelEditCell(state)
                }
            }
        }
    }

    private fun loadData(command: Command.LoadData) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val cells = generateTableUseCase(command.rows, command.columns)
            _uiState.emit(
                UiState.Data(
                    title = TextResource.idWithArgs(R.string.table_title_table_loaded, cells.size, cells.first().size),
                    cells = cells.map { it.map(Cell::toUi) },
                ),
            )
        }
    }

    private fun toggleCellColor(currentTable: UiState.Data, command: Command.ToggleCellColor): UiState {
        return currentTable.copy(
            selectedCell = command.cellId.takeIf { currentTable.selectedCell != command.cellId },
        )
    }

    private fun updateCellText(currentTable: UiState.Data, command: Command.ApplyCellEdit): UiState {
        val (rowIndex, columnIndex) = command.cellId
        return currentTable.copy(
            cells = currentTable.cells.mapIndexed { r, row ->
                if (r == rowIndex) {
                    row.mapIndexed { c, cell ->
                        if (c == columnIndex) cell.copy(text = command.text) else cell
                    }
                } else row
            },
            editDialogUiState = EditDialogUiState.Hidden,
        )
    }

    private fun editCell(tableState: UiState.Data, command: Command.EditCell): UiState.Data =
        tableState.copy(editDialogUiState = EditDialogUiState.Visible(command.cellId))

    private fun cancelEditCell(tableState: UiState.Data): UiState.Data =
        tableState.copy(editDialogUiState = EditDialogUiState.Hidden)
}
