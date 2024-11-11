package com.leic52dg17.chimp.ui.views.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.buttons.AuthenticationButton
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.inputs.AuthenticationField
import com.leic52dg17.chimp.ui.components.misc.AuthenticationTitle

@Composable
fun ForgotPasswordView(
    onForgotPassword: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
        ) {
            BackButton(modifier = modifier, onBackClick = onBackClick)
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            AuthenticationTitle(
                title = stringResource(R.string.forgot_password_text_en),
                modifier = modifier
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                AuthenticationField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.email_text_en),
                    leadingIcon = Icons.Outlined.Email,
                    leadingIconContentDescription = stringResource(R.string.email_text_en),
                    modifier = modifier.fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                AuthenticationButton(
                    onClick = { onForgotPassword(email) },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(R.string.reset_password_text_en),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordViewPreview() {
    ForgotPasswordView(
        onForgotPassword = { },
        onBackClick = { }
    )
}