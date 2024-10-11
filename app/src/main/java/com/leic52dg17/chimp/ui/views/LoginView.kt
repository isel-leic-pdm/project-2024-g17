package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.AuthenticationButton
import com.leic52dg17.chimp.ui.components.AuthenticationField
import com.leic52dg17.chimp.ui.components.AuthenticationOrDivider
import com.leic52dg17.chimp.ui.components.AuthenticationPasswordField
import com.leic52dg17.chimp.ui.components.AuthenticationTitle
import com.leic52dg17.chimp.ui.components.BackButton
import com.leic52dg17.chimp.ui.components.ForgotPasswordButton

@Composable
fun LoginView(
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
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
                title = stringResource(R.string.login_welcome_en),
                modifier = modifier
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .padding(bottom = 64.dp)
        ) {
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                AuthenticationField(
                    value = username,
                    onValueChange = { username = it },
                    label = stringResource(R.string.login_username_field_label_en),
                    leadingIcon = Icons.Outlined.PersonOutline,
                    leadingIconContentDescription = stringResource(R.string.login_username_icon_cd_en),
                    modifier = modifier.fillMaxWidth()
                )
                AuthenticationPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.login_password_field_label_en),
                    leadingIcon = Icons.Outlined.Lock,
                    leadingIconContentDescription = stringResource(R.string.login_password_icon_cd_en),
                    modifier = modifier.fillMaxWidth()
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    ForgotPasswordButton(
                        onForgotPasswordClick = { },
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                AuthenticationButton(
                    onClick = onLogInClick,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(R.string.log_in_button_text_en),
                )
                AuthenticationOrDivider(modifier = modifier)
                AuthenticationButton(
                    onClick = onSignUpClick,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    text = stringResource(R.string.sign_up_button_text_en),
                    border = BorderStroke(1.dp, Color.Gray)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginViewPreview() {
    LoginView(
        onSignUpClick = { },
        onLogInClick = { },
        onBackClick = { }
    )
}
