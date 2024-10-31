package com.leic52dg17.chimp.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun SharedAlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    confirmButtonText: String = "OK",
    alertDialogText: String = "This is the text of the alert dialog, try this out for size you cheeky monkey."
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = confirmButtonText
                )
            }
        },
        text = {
            Text(
                text = alertDialogText
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SharedAlertDialogPreview() {
    ChIMPTheme {
        SharedAlertDialog()
    }
}