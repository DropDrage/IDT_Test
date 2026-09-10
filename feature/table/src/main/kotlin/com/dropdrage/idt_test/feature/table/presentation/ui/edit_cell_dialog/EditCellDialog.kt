package com.dropdrage.idt_test.feature.table.presentation.ui.edit_cell_dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dropdrage.idt_test.core.presentation.theme.IdtTestTheme
import com.dropdrage.idt_test.feature.table.R

@Composable
internal fun EditCellDialog(
    initialText: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit,
) {
    var text by remember { mutableStateOf(initialText) }

    AlertDialog(
        title = { Text(text = stringResource(R.string.table_edit_dialog_title)) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = stringResource(R.string.table_edit_dialog_input_label)) },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) {
                Text(text = stringResource(R.string.table_edit_dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(R.string.table_edit_dialog_cancel))
            }
        },
        onDismissRequest = onCancel,
    )
}

@Preview(showBackground = true, widthDp = 400, heightDp = 500)
@Composable
private fun EditCellDialogPreview() = IdtTestTheme {
    Scaffold {
        EditCellDialog(
            initialText = "Text",
            onConfirm = {},
            onCancel = {},
        )
    }
}
