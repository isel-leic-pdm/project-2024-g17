package com.leic52dg17.chimp.ui.views.registration_invitation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import io.ktor.client.HttpClient

@Composable
fun RegistrationInvitationView(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    client: HttpClient
) {
    var token by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onBackClick = onBackClick)
        }

        Text(
            text = "Invite your friends!",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(16.dp)
        )

        Button(
            onClick = { token = "ABC"  },
            modifier = modifier.padding(16.dp)
        ) {
            Text("Generate Token")
        }

        if (token.isNotEmpty()) {
            Text(
                text = token,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.padding(16.dp)
            )

            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Token", token)
                    clipboard.setPrimaryClip(clip)
                },
                modifier = modifier.padding(16.dp)
            ) {
                Text("Copy Token")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationInvitationViewPreview() {
    RegistrationInvitationView(
        onBackClick = { } ,
                client = HttpClient()
    )
}