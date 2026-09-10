package com.dropdrage.idt_test.feature.config.presentation.ui.screen.model

internal sealed interface Command {
    class RowsChanged(val value: String): Command
    class ColumnsChanged(val value: String): Command
    object Generate : Command
}
