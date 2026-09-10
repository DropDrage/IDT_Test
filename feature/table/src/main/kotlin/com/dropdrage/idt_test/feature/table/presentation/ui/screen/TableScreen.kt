package com.dropdrage.idt_test.feature.table.presentation.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dropdrage.idt_test.common.ui.utils.TextResource
import com.dropdrage.idt_test.core.presentation.theme.IdtTestTheme
import com.dropdrage.idt_test.feature.table.R
import com.dropdrage.idt_test.feature.table.domain.model.CellId
import com.dropdrage.idt_test.feature.table.presentation.ui.edit_cell_dialog.EditCellDialog
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.CellUi
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.CellsGrid
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.Command
import com.dropdrage.idt_test.feature.table.presentation.ui.edit_cell_dialog.model.EditDialogUiState
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.UiState
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.UiState.Data
import dev.zacsweers.metrox.viewmodel.metroViewModel

private val SelectedCellColor = Color(0xFF81C784)

@Composable
fun TableScreen(
    viewModel: TableViewModel = metroViewModel(),
    rows: Int,
    columns: Int,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.command(Command.LoadData(rows, columns))
    }

    TableScreenInternal(
        state = state,
        onBack = onBack,
        onSingleClick = { row, column -> viewModel.command(Command.ToggleCellColor(CellId(row, column))) },
        onDoubleClick = { row, column -> viewModel.command(Command.EditCell(CellId(row, column))) },
        onEditConfirm = { cellId, newText -> viewModel.command(Command.ApplyCellEdit(cellId, newText)) },
        onEditCancel = { viewModel.command(Command.CancelCellEdit) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TableScreenInternal(
    state: UiState,
    onBack: () -> Unit,
    onSingleClick: (row: Int, column: Int) -> Unit,
    onDoubleClick: (row: Int, column: Int) -> Unit,
    onEditConfirm: (CellId, String) -> Unit,
    onEditCancel: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = state.title.getText())
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.table_back_arrow_content_description),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                is Data -> TableScreenState(
                    cells = state.cells,
                    selectedCell = state.selectedCell,
                    onSingleClick = onSingleClick,
                    onDoubleClick = onDoubleClick,
                )
                UiState.Loading -> LoadingScreenState()
            }
        }
    }

    if (state is Data) {
        when (state.editDialogUiState) {
            is EditDialogUiState.Visible -> {
                val cellId = state.editDialogUiState.cellId
                EditCellDialog(
                    initialText = state.cells[cellId.row][cellId.column].text,
                    onConfirm = { newText -> onEditConfirm(cellId, newText) },
                    onCancel = onEditCancel,
                )
            }
            EditDialogUiState.Hidden -> Unit
        }
    }
}

@Composable
private fun TableScreenState(
    cells: CellsGrid,
    selectedCell: CellId?,
    modifier: Modifier = Modifier,
    onSingleClick: (row: Int, column: Int) -> Unit,
    onDoubleClick: (row: Int, column: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        itemsIndexed(
            items = cells,
            key = { rowIndex, _ -> rowIndex },
        ) { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEachIndexed { columnIndex, cell ->
                    TableCell(
                        cell = cell,
                        isSelected = selectedCell == cell.cellId,
                        modifier = Modifier.weight(1f),
                        onSingleClick = { onSingleClick(rowIndex, columnIndex) },
                        onDoubleClick = { onDoubleClick(rowIndex, columnIndex) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TableCell(
    cell: CellUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSingleClick: () -> Unit,
    onDoubleClick: () -> Unit,
) {
    val cellColor = if (isSelected) SelectedCellColor else MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier
            .height(60.dp)
            .border(0.5.dp, Color.LightGray)
            .background(cellColor)
            .combinedClickable(
                onClick = onSingleClick,
                onDoubleClick = onDoubleClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = cell.text,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun LoadingScreenState(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(64.dp),
    )
}


@Preview(showBackground = true)
@Composable
private fun TableScreenPreview() = IdtTestTheme {
    TableScreenInternal(
        state = Data(
            title = TextResource.string("Title"),
            cells = listOf(
                listOf(
                    CellUi(cellId = CellId(0, 0), text = "0-0"),
                    CellUi(cellId = CellId(0, 1), text = "0-1"),
                    CellUi(cellId = CellId(0, 2), text = "0-2"),
                ),
                listOf(
                    CellUi(cellId = CellId(1, 0), text = "1-0"),
                    CellUi(cellId = CellId(1, 1), text = "1-1"),
                    CellUi(cellId = CellId(1, 2), text = "1-2"),
                ),
            ),
            editDialogUiState = EditDialogUiState.Hidden,
        ),
        onBack = {},
        onSingleClick = { _, _ -> },
        onDoubleClick = { _, _ -> },
        onEditConfirm = { _, _ -> },
        onEditCancel = {},
    )
}

@Preview(showBackground = true, widthDp = 150)
@Composable
private fun TableCellGreenPreview() = IdtTestTheme {
    Column {
        TableCell(
            cell = CellUi(cellId = CellId(0, 0), text = "0-0"),
            isSelected = false,
            onSingleClick = {},
            onDoubleClick = {},
        )

        TableCell(
            cell = CellUi(cellId = CellId(0, 0), text = "0-0"),
            isSelected = true,
            onSingleClick = {},
            onDoubleClick = {},
        )

        TableCell(
            cell = CellUi(cellId = CellId(0, 0), text = "Long long long long long long long long long long long long long long text"),
            isSelected = true,
            onSingleClick = {},
            onDoubleClick = {},
        )
    }
}
