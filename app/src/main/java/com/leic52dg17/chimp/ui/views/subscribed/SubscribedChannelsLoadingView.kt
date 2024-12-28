package com.leic52dg17.chimp.ui.views.subscribed

import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.components.misc.ChannelCardSkeleton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder
import com.leic52dg17.chimp.ui.theme.custom.topBottomBorder

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

@Composable
fun SubscribedChannelsLoadingView() {
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
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 32.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    text = stringResource(id = R.string.subscribed_channel_title_text_en),
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
                onValueChange = { },
                searchValue = "",
                color = MaterialTheme.colorScheme.background
            )
        }
        // Chats section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color.White)
                .topBottomBorder(1.dp, MaterialTheme.colorScheme.secondary)
                .heightIn(min = 800.dp)
                .fillMaxHeight()
                .fillMaxWidth()

        ) {
            for(i in 0..15) {
                ChannelCardSkeleton()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubscribedChannelsLoadingViewPreview() {
    ChIMPTheme {
        SubscribedChannelsLoadingView()
    }
}