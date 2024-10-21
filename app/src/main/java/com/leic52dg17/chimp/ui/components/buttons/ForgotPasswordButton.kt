package com.leic52dg17.chimp.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R

@Composable
fun ForgotPasswordButton(
    onForgotPasswordClick: () -> Unit,
) {
    TextButton(
        onClick = onForgotPasswordClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(R.string.login_forgot_password_text_en),
        )
    }

}