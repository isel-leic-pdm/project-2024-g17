package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.AuthenticationButton

@Composable
fun LandingView(
    modifier: Modifier = Modifier,
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            AuthenticationButton(
                onClick = onLogInClick,
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                textColor = MaterialTheme.colorScheme.secondary,
                text = stringResource(R.string.log_in_button_text_en),
                modifier = modifier,
            )

            AuthenticationButton(
                onClick = onSignUpClick,
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(R.string.sign_up_button_text_en),
                border = BorderStroke(1.dp, Color.White),
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandingViewPreview() {
    LandingView(
        onLogInClick = { },
        onSignUpClick = { }
    )
}