package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder

const val CHANNEL_CARD_TAG = "channel_card"
const val CHANNEL_NAME_TAG = "channel_name"
const val LAST_MESSAGE_TAG = "last_message"

@Composable
fun ChannelCard (
    channel: Channel,
    onChannelClick: (channel: Channel) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .clickable { onChannelClick(channel) }
            .fillMaxWidth()
            .padding(16.dp)
            .testTag(CHANNEL_CARD_TAG + "_${channel.channelId}")
    ) {
        Image(
            painter = painterResource(id = R.drawable.chimp_blue_final),
            contentDescription = stringResource(id = R.string.channel_icon_cd_en),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(50.dp)
                .clip(CircleShape)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .bottomBorder(0.5.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(200.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = channel.displayName,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.testTag(CHANNEL_NAME_TAG + "_${channel.channelId}")
                )
                Text(
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    text = if (channel.messages.isEmpty()) "" else channel.messages.last().text,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .alpha(0.60f)
                        .testTag(LAST_MESSAGE_TAG + "_${channel.channelId}")
                )
            }
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.pin_icon_cd)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelCardPreview() {
    ChIMPTheme {
        ChannelCard(
            channel = FakeData.channels[0],
            onChannelClick = { }
        )
    }
}