package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.AuthenticationButton
import com.leic52dg17.chimp.ui.components.AuthenticationOrDivider

@Composable
fun SignUpView(
    onSignUpClick: () -> Unit,
    onLogInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username = ""
    var password = ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sign_up_page_welcome_en),
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = modifier
                    .padding(horizontal = 16.dp)
            )
        }

        Column(
            modifier = modifier
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
            )
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
                onClick = onLogInClick,
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                textColor = MaterialTheme.colorScheme.secondary,
                text = stringResource(R.string.log_in_button_text_en),
                border = BorderStroke(1.dp, Color.Gray)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpViewPreview() {
    SignUpView(
        onSignUpClick = { },
        onLogInClick = { },
    )
}
