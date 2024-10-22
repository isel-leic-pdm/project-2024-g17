/*
package com.leic52dg17.chimp.ui.views.channel

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.User
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.message.Message
import java.math.BigInteger
import java.time.Instant
import androidx.compose.material3.Icon
import androidx.compose.ui.res.stringResource
import com.leic52dg17.chimp.ui.components.BackButton


@Composable
fun ChannelInfoView(
    channel: Channel,
    onBackClick: () -> Unit,
    onAddToUserChannelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
        ) {
            BackButton(modifier = modifier, onBackClick = onBackClick)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Column {
                Image(
//                    painter = rememberAsyncImagePainter(channel.channelIconUrl),
                    painter = painterResource(id = R.mipmap.chimp_white_logo_foreground),
                    contentDescription = "Channel Image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .clip(CircleShape)
                        .size(300.dp)
                )
                Text(
                    text = channel.displayName,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = "Users subscribed to ${channel.displayName}",
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                modifier = modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 16.dp)
            )
            for (user in channel.users) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(1.dp, Color.Black)
                ) {
                    Column {
                        Text(
                            text = user.displayName,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        )
                        Text(
                            text = user.username,
                            fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        )
                    }
                    Column {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "More"
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onAddToUserChannelClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = RoundedCornerShape(20),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) { Text(stringResource(R.string.add_user_to_channel_en)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelInfoViewPreview() {
    ChannelInfoView(
        Channel(
            channelId = 1,
            displayName = "Channel Name",
            channelIconUrl = "https://picsum.photos/300/300",
            users = listOf(
                User(1, "User1", "displayName1"),
                User(2, "User2", "displayName2"),
                User(3, "User3", "displayName3")
            ),
            messages = listOf(
                Message(1,1, "Hello", BigInteger.valueOf(Instant.now().toEpochMilli())),
                Message(2,1, "Hi", BigInteger.valueOf(Instant.now().toEpochMilli())),
                Message(1, 1, "How are you", BigInteger.valueOf(Instant.now().toEpochMilli()))
            ),
            channelIconContentDescription = null
        ),
        onBackClick = { },
        onAddToUserChannelClick = {}
    )
}*/
