package com.dropdrage.idt_test.feature.config.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.dropdrage.idt_test.common.ui.utils.TextResource
import com.dropdrage.idt_test.core.presentation.theme.IdtTestTheme
import com.dropdrage.idt_test.feature.config.R
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.Command
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.News
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.UiState
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = metroViewModel(),
    onNavigateToTable: (rows: Int, columns: Int) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.news.collect { news ->
            when (news) {
                News.Generate -> {
                    onNavigateToTable(state.rows.toInt(), state.columns.toInt())
                }
            }
        }
    }

    ConfigScreenInternal(
        state = state,
        onRowsChanged = { viewModel.command(Command.RowsChanged(it)) },
        onColumnsChanged = { viewModel.command(Command.ColumnsChanged(it)) },
        onGenerateClicked = { viewModel.command(Command.Generate) },
    )
}

@Composable
private fun ConfigScreenInternal(
    state: UiState,
    onRowsChanged: (String) -> Unit,
    onColumnsChanged: (String) -> Unit,
    onGenerateClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.widthIn(min = 300.dp, max = 400.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.config_title),
                    style = MaterialTheme.typography.headlineSmall,
                )

                OutlinedTextField(
                    value = state.rows,
                    onValueChange = onRowsChanged,
                    label = { Text(text = stringResource(R.string.config_row_input_hint)) },
                    isError = state.rowsError != null,
                    supportingText = state.rowsError?.let { { Text(it.getText()) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = state.columns,
                    onValueChange = onColumnsChanged,
                    label = { Text(text = stringResource(R.string.config_column_input_hint)) },
                    isError = state.columnsError != null,
                    supportingText = state.columnsError?.let { { Text(it.getText()) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )

                Button(
                    onClick = onGenerateClicked,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.config_generate_button))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ConfigScreenWithErrorsPreview(
    @PreviewParameter(ConfigScreenPreviewParameters::class) uiState: UiState,
) = IdtTestTheme {
    ConfigScreenInternal(
        state = uiState,
        onRowsChanged = {},
        onColumnsChanged = {},
        onGenerateClicked = {},
    )
}

private class ConfigScreenPreviewParameters : CollectionPreviewParameterProvider<UiState>(
    listOf(
        UiState(
            rows = "12",
            columns = "14",
        ),
        UiState(
            rows = "12",
            columns = "14",
            rowsError = TextResource.string("Error"),
            columnsError = TextResource.string("Error"),
        ),
    ),
) {
    override fun getDisplayName(index: Int): String? {
        return when (index) {
            0 -> "No error"
            1 -> "With errors"
            else -> super.getDisplayName(index)
        }
    }
}
