package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun SendEmailView() {

    var currentEmailAddressValue by remember { mutableStateOf("") }
    var currentEmailContentValue by remember { mutableStateOf("") }

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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }
        }
        Text(
            modifier = Modifier
                .size(400.dp, 150.dp)
                .padding(16.dp),
            text = stringResource(id = R.string.about_send_email_title_text_en),
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            lineHeight = MaterialTheme.typography.displaySmall.lineHeight,
            fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
            textAlign = TextAlign.Center
        )
        // Input section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .size(300.dp, 80.dp)
                    .padding(bottom = 16.dp),
                value = currentEmailAddressValue,
                onValueChange = { currentEmailAddressValue = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.about_send_email_address_label_text_en),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                    )
                },
            )
            TextField(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .size(300.dp, 200.dp),
                value = currentEmailContentValue,
                onValueChange = { currentEmailContentValue = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.about_send_email_content_label_text_en),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                    )
                },
            )
        }
    }
}

@Preview
@Composable
fun SendEmailViewPreview() {
    ChIMPTheme {
        SendEmailView()
    }
}