package com.leic52dg17.chimp.ui.views.authentication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.buttons.AuthenticationButton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun LandingView(
    modifier: Modifier = Modifier,
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
        ,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .padding(top = 250.dp)
        ) {
            val imageResource = R.mipmap.chimp_white_logo_foreground

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = modifier.size(300.dp)
            )
        }

        Column(
            modifier = modifier
                .padding(bottom = 50.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.landing_welcome_message_en),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.displaySmall.fontSize,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Text(
                    text = stringResource(R.string.landing_description_en),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 5.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                AuthenticationButton(
                    onClick = onLogInClick,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    text = stringResource(R.string.log_in_button_text_en),
                )

                AuthenticationButton(
                    onClick = onSignUpClick,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(R.string.sign_up_button_text_en),
                    border = BorderStroke(1.dp, Color.White),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandingViewPreview() {
    ChIMPTheme {
        LandingView(
            onLogInClick = { },
            onSignUpClick = { }
        )
    }
}