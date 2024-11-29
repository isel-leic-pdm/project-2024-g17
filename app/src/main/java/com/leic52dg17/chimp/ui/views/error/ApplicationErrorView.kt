package com.leic52dg17.chimp.ui.views.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.ui.components.dialogs.SharedAlertDialog
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun ApplicationErrorView(
    message: String,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        SharedAlertDialog(
            alertDialogText = message,
            onDismissRequest = onDismiss
        )
    }
}


@Composable
@Preview
fun ApplicationErrorPreview() {
    ChIMPTheme {
        ApplicationErrorView(
            message = "An authenticated user could not be found. You'll now be logged out.",
            {})
    }
}