package com.leic52dg17.chimp.ui.views.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun ApplicationErrorView(
    message: String,
    onDismiss: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Icon(
            modifier = Modifier
                .size(128.dp),
            imageVector = Icons.Filled.Error,
            contentDescription = "Error icon",
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = "Error"
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(32.dp),
            text = message
        )
        Button(onClick = { onDismiss() }) {
            Text(text = "OK")
        }
    }
}


@Composable
@Preview
fun ApplicationErrorPreview() {
    ChIMPTheme {
        ApplicationErrorView(
            message = "An authenticated user could not be found.\nYou'll now be logged out.",
            {})
    }
}