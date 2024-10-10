package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.leic52dg17.chimp.ui.components.BackButton

@Composable
fun SignUpView(
    onSignUpClick: () -> Unit,
    onLogInClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxSize(),
    ) {
        BackButton(modifier = modifier, onBackClick = onBackClick)

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 150.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_up_welcome_en),
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = modifier
                    .padding(horizontal = 16.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(bottom = 25.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 50.dp)
            ) {
                AuthenticationField(
                    value = username,
                    onValueChange = { username = it },
                    label = stringResource(R.string.sign_up_username_field_label_en),
                    leadingIcon = Icons.Outlined.PersonOutline,
                    leadingIconContentDescription = stringResource(R.string.sign_up_username_icon_cd_en),
                    modifier = modifier.fillMaxWidth()
                )
                AuthenticationPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.sign_up_password_field_label_en),
                    leadingIcon = Icons.Outlined.Lock,
                    leadingIconContentDescription = stringResource(R.string.sign_up_password_icon_cd_en),
                    modifier = modifier
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                AuthenticationButton(
                    onClick = onSignUpClick,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(R.string.sign_up_button_text_en),
                )
                AuthenticationOrDivider(modifier = modifier)
                AuthenticationButton(
                    onClick = onLogInClick,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    text = stringResource(R.string.log_in_button_text_en),
                    border = BorderStroke(1.dp, Color.Gray)
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpViewPreview() {
    SignUpView(
        onSignUpClick = { },
        onLogInClick = { },
        onBackClick = { }
    )
}
