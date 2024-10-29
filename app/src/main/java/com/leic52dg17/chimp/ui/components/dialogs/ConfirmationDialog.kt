package com.leic52dg17.chimp.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun ConfirmationDialog(
    confirmationTitle: String = "Title",
    confirmationText: String = "Do you really wish to perform this action?",
    confirmButtonText: String = "Yes",
    cancelButtonText: String = "No",
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = confirmationTitle
            )
        },
        text = {
            Text(
                text = confirmationText
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = confirmButtonText
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = cancelButtonText
                )
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfirmationDialogPreview() {
    ChIMPTheme {
        ConfirmationDialog()
    }
}