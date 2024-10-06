package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.theme.ChIMPTheme


val authorNames: Array<String> = arrayOf("João Cardoso", "Francisco Antunes", "Rúben Said")

@Composable
fun AboutView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(500.dp, 50.dp)

        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 100.dp)
        ){
            // Logo section
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = ColorPainter(Color.Blue),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 62.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(200.dp, 80.dp)
                )
            }

            // App version section
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.about_version_number_title_text_en),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.app_version_number),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .alpha(0.5f)
                )
            }

            // Author section
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.about_authors_title_text_en),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                for (authorName in authorNames) {
                    Text(
                        text = authorName,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .alpha(0.5f)
                    )
                }
            }
            // Button section
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(170.dp, 50.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = stringResource(R.string.about_send_email_button_text_en),
                    )
                }
                Button(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(170.dp, 50.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = stringResource(id = R.string.about_privacy_policy_button_text_en)
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun AboutViewPreview() {
    ChIMPTheme {
        AboutView()
    }
}