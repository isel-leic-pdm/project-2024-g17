package com.leic52dg17.chimp.ui.views.authentication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
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
import com.leic52dg17.chimp.ui.components.buttons.AuthenticationButton
import com.leic52dg17.chimp.ui.components.inputs.AuthenticationField
import com.leic52dg17.chimp.ui.components.misc.AuthenticationOrDivider
import com.leic52dg17.chimp.ui.components.inputs.AuthenticationPasswordField
import com.leic52dg17.chimp.ui.components.misc.AuthenticationTitle
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.buttons.ForgotPasswordButton

@Composable
fun ChangePasswordView(
    onChangePassword: (String, String, String, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
){

    var username by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


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
                title = stringResource(R.string.change_password_text_en),
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
                AuthenticationPasswordField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = stringResource(R.string.current_password_text_en),
                    leadingIcon = Icons.Outlined.LockOpen,
                    leadingIconContentDescription = stringResource(R.string.login_password_icon_cd_en),
                    modifier = modifier.fillMaxWidth()
                )
                AuthenticationPasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = stringResource(R.string.new_password_text_en),
                    leadingIcon = Icons.Outlined.Lock,
                    leadingIconContentDescription = stringResource(R.string.login_password_icon_cd_en),
                    modifier = modifier.fillMaxWidth()
                )
                AuthenticationPasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = stringResource(R.string.confirm_password_text_en),
                    leadingIcon = Icons.Outlined.Lock,
                    leadingIconContentDescription = stringResource(R.string.login_password_icon_cd_en),
                    modifier = modifier.fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                AuthenticationButton(
                    onClick = { onChangePassword(username, currentPassword, newPassword, confirmPassword) },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(R.string.change_password_text_en),
                )
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangePasswordViewPreview() {
        ChangePasswordView(
            onChangePassword = { _, _, _, _ -> },
            onBackClick = { }
        )
}

