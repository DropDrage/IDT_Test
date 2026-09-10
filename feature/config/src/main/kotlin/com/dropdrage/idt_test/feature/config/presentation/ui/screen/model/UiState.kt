package com.dropdrage.idt_test.feature.config.presentation.ui.screen.model

import com.dropdrage.idt_test.common.ui.utils.TextResource

internal data class UiState(
    val rows: String = "",
    val rowsError: TextResource? = null,
    val columns: String = "",
    val columnsError: TextResource? = null,
)
