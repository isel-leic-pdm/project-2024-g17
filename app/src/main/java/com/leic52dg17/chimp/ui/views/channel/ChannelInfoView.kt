package com.leic52dg17.chimp.ui.views.channel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.leic52dg17.chimp.model.User
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.message.Message
import java.math.BigInteger
import java.time.Instant

@Composable
fun ChannelInfoView(
    channel: Channel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(channel.channelIconUrl),
                contentDescription = "Channel Image"
            )
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
        Channel(
            channelId = 1,
            displayName = "Channel Name",
            channelIconUrl = "https://picsum.photos/300/300",
            users = listOf(
                User(1, "User1", "displayname1"),
                User(2, "User2", "displayname2")
            ),
            messages = listOf(
                Message(1,1, "Hello", BigInteger.valueOf(Instant.now().toEpochMilli())),
                Message(2,1, "Hi", BigInteger.valueOf(Instant.now().toEpochMilli())),
                Message(1, 1, "How are you", BigInteger.valueOf(Instant.now().toEpochMilli()))
            ),
            channelIconContentDescription = null
        )
    )
}