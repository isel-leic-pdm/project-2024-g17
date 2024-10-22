package com.leic52dg17.chimp.ui.views.subscribed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.common.ImageResource
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder
import com.leic52dg17.chimp.ui.theme.custom.topBottomBorder

val channelList = listOf(
    Channel(
        channelId = 1,
        displayName = "Channel 1",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 1 Icon"
    ),
    Channel(
        channelId = 2,
        displayName = "Channel 2",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 2 Icon"
    ),
    Channel(
        channelId = 3,
        displayName = "Channel 3",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 3 Icon"
    ),
    Channel(
        channelId = 4,
        displayName = "Channel 4",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 4 Icon"
    ),
    Channel(
        channelId = 5,
        displayName = "Channel 4",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 5 Icon"
    ),
    Channel(
        channelId = 6,
        displayName = "Channel 6",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 6 Icon"
    ),
    Channel(
        channelId = 7,
        displayName = "Channel 7",
        messages = emptyList(),
        users = emptyList(),
        "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 7 Icon"
    ),
    Channel(
        channelId = 8,
        displayName = "Channel 8",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 8 Icon"
    ),
    Channel(
        channelId = 9,
        displayName = "Channel 9",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 9 Icon"
    ),
    Channel(
        channelId = 10,
        displayName = "Channel 10",
        messages = emptyList(),
        users = emptyList(),
        channelIconUrl = "https://picsum.photos/300/300",
        channelIconContentDescription = "Channel 10 Icon"
    ),
)

@Composable
fun SubscribedChannelsView(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onCreateChannelClick: () -> Unit = {}
) {

    var searchValue by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(innerPadding)
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
                .fillMaxHeight()
                .fillMaxWidth()

        ) {
            for (channel in channelList) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
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
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .bottomBorder(0.5.dp, MaterialTheme.colorScheme.secondary)
                    ) {
                        Column {
                            Text(
                                textAlign = TextAlign.Center,
                                text = channel.displayName,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                            )
                            Text(
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                text = channel.messages.last().text,
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

@Preview
@Composable
fun SubscribedChannelsViewPreview() {
    ChIMPTheme {
        SubscribedChannelsView()
    }
}