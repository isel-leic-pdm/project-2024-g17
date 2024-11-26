package com.leic52dg17.chimp.ui.views.channel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder
import com.leic52dg17.chimp.ui.theme.custom.topBottomBorder
import java.math.BigInteger
import java.time.Instant

@Composable
fun ChannelInfoView(
    channel: Channel,
    onBackClick: () -> Unit = {},
    onAddToUserChannelClick: () -> Unit = {},
    onRemoveUser: (Int, Int) -> Unit = { _, _ -> },
    onUserClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    authenticatedUser: AuthenticatedUser? = null,
    onLeaveChannelClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BackButton(onBackClick = onBackClick)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(channel.channelIconUrl),
                    contentDescription = "Channel Image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .clip(CircleShape)
                        .size(200.dp)
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
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Users subscribed to ${channel.displayName}:",
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                modifier = modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 16.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                        .topBottomBorder(1.dp, MaterialTheme.colorScheme.secondary)
                        .heightIn(min = 300.dp, max = 300.dp)
                        .padding(bottom = 15.dp)
                ) {
                    for (user in channel.users) {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable(onClick = { onUserClick(user.id) })
                                .bottomBorder(0.2.dp, MaterialTheme.colorScheme.secondary)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = user.displayName,
                                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        )
                                        if (authenticatedUser?.user?.id == user.id) {
                                            Icon(
                                                modifier = Modifier
                                                    .padding(horizontal = 8.dp),
                                                imageVector = Icons.Filled.AdminPanelSettings,
                                                contentDescription = "Owner icon"
                                            )
                                        }
                                    }
                                    Text(
                                        text = "@" + user.username,
                                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                                        fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                    )
                                }
                                Column {
                                    if (authenticatedUser?.user?.id != user.id && authenticatedUser?.user?.id == channel.ownerId) {
                                        IconButton(
                                            onClick = {
                                                onRemoveUser(user.id, channel.channelId)
                                            }) {
                                            Icon(
                                                imageVector = Icons.Filled.Remove,
                                                contentDescription = "More"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Column {
                    if (authenticatedUser?.user?.id == channel.ownerId) {
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
                        ) {
                            Text(stringResource(R.string.add_user_to_channel_en))
                        }
                    }
                    Button(
                        onClick = { onLeaveChannelClick() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        shape = RoundedCornerShape(20),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)

                    ) {
                        Text(stringResource(id = R.string.leave_channel_en))
                    }
                }
            }
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
            ),
            messages = listOf(
                Message(1, 1, 1, "Hello", Instant.now().toEpochMilli()),
                Message(2, 2, 1, "Hi", Instant.now().toEpochMilli()),
                Message(3, 1, 1, "How are you", Instant.now().toEpochMilli())
            ),
            isPrivate = true,
            ownerId = 1
        ),
        authenticatedUser = AuthenticatedUser(
            "abc",
            User(1, "User1", "displayName1")
        )
    )
}
