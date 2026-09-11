package com.dropdrage.idt_test.feature.config.presentation.ui.screen

import androidx.lifecycle.ViewModel
import com.dropdrage.idt_test.common.ui.utils.TextResource
import com.dropdrage.idt_test.feature.config.R
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.Command
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.News
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.UiState
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.validator.RangeValidator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Named
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class ConfigViewModel(
    @Named("rowsValidator") private val rowsValidator: RangeValidator<Int>,
    @Named("columnsValidator") private val columnsValidator: RangeValidator<Int>,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    internal val uiState = _uiState.asStateFlow()

    private val _news = MutableSharedFlow<News>(extraBufferCapacity = 1)
    internal val news = _news.asSharedFlow()


    internal fun command(command: Command) {
        when (command) {
            Command.Generate -> validateAndGenerate(_uiState.value)
            else -> _uiState.update { state ->
                when (command) {
                    is Command.RowsChanged -> onRowsChanged(state, command)
                    is Command.ColumnsChanged -> onColumnsChanged(state, command)
                }
            }
        }
    }

    private fun onRowsChanged(state: UiState, command: Command.RowsChanged): UiState {
        val filtered = command.value.filter { it.isDigit() }
        val value = filtered.toIntOrNull()
        return state.copy(
            rows = filtered,
            rowsError = validateRows(value),
        )
    }

    private fun validateRows(value: Int?): TextResource? =
        if (!rowsValidator.isValid(value)) TextResource.id(R.string.config_row_validation_error)
        else null

    private fun onColumnsChanged(state: UiState, command: Command.ColumnsChanged): UiState {
        val filtered = command.value.filter { it.isDigit() }
        val value = filtered.toIntOrNull()
        return state.copy(
            columns = filtered,
            columnsError = validateColumns(value),
        )
    }

    private fun validateColumns(value: Int?): TextResource? =
        if (!columnsValidator.isValid(value)) TextResource.id(R.string.config_column_validation_error)
        else null

    private fun validateAndGenerate(state: UiState) {
        val validatedState = validateState(state)
        if (validatedState.rowsError == null && validatedState.columnsError == null) {
            _news.tryEmit(News.Generate)
        } else {
            _uiState.tryEmit(validatedState)
        }
    }

    private fun validateState(state: UiState): UiState = state.copy(
        rowsError = validateRows(state.rows.toIntOrNull()),
        columnsError = validateColumns(state.columns.toIntOrNull()),
    )
}
