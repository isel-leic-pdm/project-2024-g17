package com.leic52dg17.chimp.ui.views.subscribed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Create
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.components.misc.ChannelCard
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.topBottomBorder

const val CREATE_CHANNEL_BUTTON_TAG = "create_channel_button"
const val SEARCH_BAR_TAG = "search_bar"

@Composable
fun SubscribedChannelsView(
    channels: List<Channel>,
    onCreateChannelClick: () -> Unit = {},
    onChannelClick: (Int) -> Unit = {}
) {
    var searchValue by remember { mutableStateOf("") }
    val filteredChannels =
        remember(channels, searchValue) {  // Key on both channels and searchValue
            channels.filter { it.displayName.contains(searchValue, ignoreCase = true) }
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
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
                painter = painterResource(id = R.drawable.chimp_white_final),
                contentDescription = stringResource(id = R.string.app_logo_cd)
            )
            Text(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                text = stringResource(id = R.string.subscribed_channel_title_text_en),
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(
                modifier = Modifier.testTag(CREATE_CHANNEL_BUTTON_TAG),
                onClick = {
                    onCreateChannelClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = MaterialTheme.colorScheme.onPrimary,
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
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .size(500.dp, 50.dp)
                    .testTag(SEARCH_BAR_TAG),
                placeHolderFontSize = MaterialTheme.typography.bodySmall.fontSize,
                onValueChange = { newValue -> searchValue = newValue },
                searchValue = searchValue,
                color = MaterialTheme.colorScheme.background
            )
        }
        // Chats section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .topBottomBorder(2.dp, MaterialTheme.colorScheme.secondary)
                .heightIn(min = 800.dp)
                .fillMaxHeight()
                .fillMaxWidth()

        ) {
            val toDisplay = filteredChannels.ifEmpty {
                channels
            }

            for (channel in toDisplay) {
                ChannelCard(
                    channel = channel
                ) {
                    onChannelClick(channel.channelId)
                }
            }
        }
    }
}

private val mockChannelList = listOf(
    Channel(
        channelId = 1,
        displayName = "very very long name",
        messages = listOf(
            Message(
                id = 1,
                userId = 1,
                channelId = 1,
                text = "Message 1",
                createdAt = 1640995200L
            )
        ),
        users = listOf(
            User(
                id = 1,
                username = "user1",
                displayName = "User 1"
            ),
            User(
                id = 2,
                username = "user2",
                displayName = "User 2"
            )
        ),
        channelIconUrl = "https://picsum.photos/300/300",
        isPrivate = false,
        ownerId = 2
    ),
    Channel(
        channelId = 2,
        displayName = "short",
        messages = listOf(
            Message(
                id = 2,
                userId = 1,
                channelId = 1,
                text = "Message 1",
                createdAt = 1640995200L
            )
        ),
        users = listOf(
            User(
                id = 1,
                username = "user1",
                displayName = "User 1"
            ),
            User(
                id = 2,
                username = "user2",
                displayName = "User 2"
            )
        ),
        channelIconUrl = "https://picsum.photos/300/300",
        isPrivate = true,
        ownerId = 1
    )
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubscribedChannelsViewPreview() {
    ChIMPTheme {
        SubscribedChannelsView(mockChannelList)
    }
}