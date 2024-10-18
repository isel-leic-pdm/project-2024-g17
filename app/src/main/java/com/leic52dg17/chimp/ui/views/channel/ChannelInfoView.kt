package com.leic52dg17.chimp.ui.views.channel

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.channel.Channel

@Composable
fun ChannelInfoView(
    channel: Channel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
//            Image(
//                painter = painterResource(id = R.drawable.channel_image),
//                contentDescription = "Channel Image"
//            )
            Text(
                text = channel.displayName,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
                fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
                fontSize = MaterialTheme.typography.displayLarge.fontSize
            )
        }
    }
}

@Preview
@Composable
fun ChannelInfoViewPreview() {
    ChannelInfoView(
        channel = Channel(
            displayName = "Channel Name",
            channelIcon = R.mipmap.chimp_white_logo_foreground,

        )
    )
}