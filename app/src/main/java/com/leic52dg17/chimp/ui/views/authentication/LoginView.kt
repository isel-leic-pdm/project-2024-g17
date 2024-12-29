package com.leic52dg17.chimp.ui.views.authentication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.ui.components.buttons.AuthenticationButton
import com.leic52dg17.chimp.ui.components.inputs.AuthenticationField
import com.leic52dg17.chimp.ui.components.misc.AuthenticationOrDivider
import com.leic52dg17.chimp.ui.components.inputs.AuthenticationPasswordField
import com.leic52dg17.chimp.ui.components.misc.AuthenticationTitle
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.buttons.ForgotPasswordButton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun LoginView(
    onLogInClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var currentErrors by rememberSaveable {
        mutableStateOf<Map<String, Pair<Boolean, String?>>>(emptyMap())
    }

    fun removeErrorsForKey(key: String) {
        currentErrors = currentErrors.filterNot { it.key == key }
    }

    fun validateForm(): Boolean {
        val errors = mutableMapOf<String, Pair<Boolean, String?>>()

        if (username.isBlank()) {
            errors["username"] = Pair(true, ErrorMessages.USERNAME_BLANK)
        }
        if (password.isBlank()) {
            errors["password"] = Pair(true, ErrorMessages.PASSWORD_BLANK)
        }

        currentErrors = errors

        return errors.isEmpty()
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
        ) {
            BackButton(modifier = modifier, onBackClick = onBackClick, color = MaterialTheme.colorScheme.onPrimary)
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            AuthenticationTitle(
                title = stringResource(R.string.login_welcome_en),
                modifier = modifier,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 64.dp)
            ) {
                AuthenticationField(
                    value = username,
                    onValueChange = {
                        removeErrorsForKey("username")
                        username = it
                    },
                    label = stringResource(R.string.login_username_field_label_en),
                    leadingIcon = Icons.Outlined.PersonOutline,
                    leadingIconContentDescription = stringResource(R.string.login_username_icon_cd_en),
                    modifier = modifier.fillMaxWidth(),
                    isError = currentErrors.keys.any { it == "username" },
                    supportingText = currentErrors["username"]?.second ?: ""
                )
                AuthenticationPasswordField(
                    value = password,
                    onValueChange = {
                        removeErrorsForKey("password")
                        password = it
                    },
                    label = stringResource(R.string.login_password_field_label_en),
                    leadingIcon = Icons.Outlined.Lock,
                    leadingIconContentDescription = stringResource(R.string.login_password_icon_cd_en),
                    modifier = modifier.fillMaxWidth(),
                    isError = currentErrors.keys.any { it == "password" },
                    supportingText = currentErrors["password"]?.second ?: ""
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    ForgotPasswordButton(
                        onForgotPasswordClick = onForgotPasswordClick,
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            ) {
                AuthenticationButton(
                    onClick = {
                        val isValid = validateForm()
                        if (isValid) {
                            onLogInClick(username, password)
                        }
                    },
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
    ChIMPTheme {
        LoginView(
            onSignUpClick = {},
            onLogInClick = { username, password -> println(username); println(password) },
            onBackClick = {},
            onForgotPasswordClick = {}
        )
    }
}
