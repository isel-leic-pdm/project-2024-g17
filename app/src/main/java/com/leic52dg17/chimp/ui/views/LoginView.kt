package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.AuthenticationButton

@Composable
fun LandingView(
    modifier: Modifier = Modifier,
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = ColorPainter(Color.Blue),
                contentDescription = null,
                modifier = modifier
                    .size(200.dp)
            )
        }

        Column(
            modifier = modifier.padding(bottom = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.landing_welcome_message_en),
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Text(
                text = stringResource(R.string.landing_description_en),
                fontSize = 15.sp,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            AuthenticationButton(
                onClick = onLogInClick,
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                textColor = MaterialTheme.colorScheme.secondary,
                text = stringResource(R.string.log_in_button_text_en),
                border = BorderStroke(1.dp, Color.Black), // REMOVE LATER
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