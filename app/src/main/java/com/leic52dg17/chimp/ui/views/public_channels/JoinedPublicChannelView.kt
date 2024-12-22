package com.leic52dg17.chimp.ui.views.public_channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun JoinedPublicChannelView(
    channelName: String,
    onConfirm: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            modifier = Modifier
                .size(200.dp),
            imageVector = Icons.Filled.CheckCircle,
            tint = Color.Green,
            contentDescription = stringResource(id = R.string.green_check_icon_cd_en)
        )
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            text = "Successfully joined $channelName!"
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(onClick = {
            onConfirm()
        }) {
            Text(text = "OK")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JoinedPublicChannelPreview() {
    ChIMPTheme {
        JoinedPublicChannelView(
            "Test",
            {}
        )
    }
}