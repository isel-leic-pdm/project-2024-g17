package com.leic52dg17.chimp.ui.views.subscribed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.message.Message
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder
import com.leic52dg17.chimp.ui.theme.custom.topBottomBorder

val mockChannelList = listOf(
    Channel(
        channelId = 1,
        displayName = "very very long name",
        messages = listOf(
            Message(
                userId = 1,
                channelId = 1,
                text = "Message 1",
                createdAt = 1640995200.toBigInteger()
            )
        ),
        users = listOf(
            User(
                userId = 1,
                username = "user1",
                displayName = "User 1"
            ),
            User(
                userId = 2,
                username = "user2",
                displayName = "User 2"
            )
        ),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 1 Icon",
        isPrivate = false,
        ownerId = 2
    ),
    Channel(
        channelId = 2,
        displayName = "short",
        messages = listOf(
            Message(
                userId = 1,
                channelId = 1,
                text = "Message 1",
                createdAt = 1640995200.toBigInteger()
            )
        ),
        users = listOf(
            User(
                userId = 1,
                username = "user1",
                displayName = "User 1"
            ),
            User(
                userId = 2,
                username = "user2",
                displayName = "User 2"
            )
        ),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 2 Icon",
        isPrivate = true,
        ownerId = 1
    )
)

@Composable
fun SubscribedChannelsView(
    channels: List<Channel>? = mockChannelList,
    onCreateChannelClick: () -> Unit = {},
    onChannelClick: (Channel) -> Unit = {}
) {

    var searchValue by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .fillMaxSize()
    ) {
        // Top section
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 32.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp),
                painter = painterResource(id = R.drawable.chimp_blue_final),
                contentDescription = stringResource(id = R.string.app_logo_cd)
            )
            Text(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                text = stringResource(id = R.string.subscribed_channel_title_text_en),
            )
            IconButton(
                onClick = {
                    onCreateChannelClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(id = R.string.search_icon_cd)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        ) {
            SearchBar(
                textFieldModifier = Modifier
                    .padding(horizontal = 32.dp)
                    .size(500.dp, 50.dp),
                placeHolderFontSize = MaterialTheme.typography.bodySmall.fontSize,
                onValueChange = { searchValue = it },
                searchValue = searchValue
            )
        }
        // Chats section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .topBottomBorder(2.dp, MaterialTheme.colorScheme.secondary)
                .heightIn(min = 800.dp)
                .fillMaxHeight()
                .fillMaxWidth()

        ) {
            if (channels != null) {
                for (channel in channels) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .clickable { onChannelClick(channel) }
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.chimp_blue_final),
                            contentDescription = channel.channelIconContentDescription,
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
                                )
                                Text(
                                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                    text = if(channel.messages.isEmpty()) "" else channel.messages.last().text,
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                        .alpha(0.60f)
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
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubscribedChannelsViewPreview() {
    ChIMPTheme {
        SubscribedChannelsView()
    }
}