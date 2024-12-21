package com.leic52dg17.chimp.ui.views.registration_invitation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.misc.Skeleton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun RegistrationInvitationLoadingView(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            onClick = { },
            enabled = false,
            modifier = modifier.padding(16.dp)
        ) {
            Text("Generate Token")
        }

        Skeleton(
            sizeModifier = Modifier
                .height(32.dp)
                .width(256.dp)
        )

        Button(
            modifier = modifier.padding(16.dp),
            onClick = {},
            enabled = false
        ) {
            Text("Copy Token")
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegistrationInvitationLoadingPreview() {
    ChIMPTheme {
        RegistrationInvitationLoadingView(onBackClick = { })
    }
}