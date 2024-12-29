package com.leic52dg17.chimp.ui.views.channel


import MessageTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.utils.formatHours
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.domain.utils.deriveDateFrom
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

const val BACK_BUTTON_TAG = "back_button"
const val CHANNEL_NAME_TAG = "channel_name"
const val MESSAGES_COLUMN_TAG = "messages_column"
const val MESSAGE_BOX_TAG = "message_box"
const val SEND_BUTTON_TAG = "send_button"

@Composable
fun ChannelMessageView(
    channel: Channel,
    onBackClick: () -> Unit,
    onChannelNameClick: () -> Unit,
    onSendClick: (String) -> Unit,
    hasWritePermissions: Boolean,
    authenticatedUser: AuthenticatedUser?
) {
    var textFieldWidth by remember { mutableStateOf(400.dp) }
    var messageText by remember { mutableStateOf("") }
    val isSendIconVisible = messageText.isNotEmpty() && hasWritePermissions
    val listState = rememberLazyListState()

    LaunchedEffect(channel.messages.size) {
        if (channel.messages.isNotEmpty()) {
            listState.scrollToItem(channel.messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onBackClick = onBackClick, color = MaterialTheme.colorScheme.onPrimary)
            Row(
                modifier = Modifier
                    .clickable { onChannelNameClick() }
                    .testTag(CHANNEL_NAME_TAG),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = channel.displayName,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 27.sp
                )
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Circle,
                        contentDescription = stringResource(id = R.string.back_button_text_cd),
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp)
                .testTag(MESSAGES_COLUMN_TAG),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 256.dp)
        ) {
            items(channel.messages) { message ->
                val backgroundColor = if (message.userId == authenticatedUser?.user?.id) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                }
                val horizontalArrangement =
                    if (message.userId == authenticatedUser?.user?.id) Arrangement.End else Arrangement.Start

                val paddingVal = if (message.userId == authenticatedUser?.user?.id) {
                    PaddingValues(end = 10.dp, start = 40.dp)
                } else {
                    PaddingValues(start = 10.dp, end = 40.dp)
                }

                Row(
                    modifier = Modifier
                        .padding(paddingVal)
                        .fillMaxWidth(),
                    horizontalArrangement = horizontalArrangement,

                    ) {
                    Surface(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(8.dp))
                            .wrapContentSize(),
                        shape = RoundedCornerShape(8.dp),
                        color = backgroundColor
                    ) {
                        // TODO: Refactor so we get the actual users name
                        val userDisplayName =
                            channel.users.firstOrNull { it.id == message.userId }?.displayName
                                ?: "User ${message.userId}"
                        val alignment =
                            if (message.userId == authenticatedUser?.user?.id) Alignment.End else Alignment.Start
                        Column(
                            horizontalAlignment = alignment,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = message.text,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier,
                            ) {
                                Text(
                                    fontSize = 12.sp,
                                    text = userDisplayName,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                )
                                Text(
                                    text = formatHours(message.createdAt),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                )
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .align(
                        CenterHorizontally
                    ),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.tertiary

            ) {
                if (channel.messages.isNotEmpty()) {
                    val firstVisibleMessageIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
                    Text(
                        text = channel.messages[firstVisibleMessageIndex].createdAt.deriveDateFrom(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
                .align(Alignment.BottomEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val imageResource = painterResource((R.drawable.paper_clip_icon))
            Image(
                painter = imageResource,
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            if (isSendIconVisible) textFieldWidth = 300.dp
            MessageTextField(
                modifier = Modifier
                    .width(textFieldWidth)
                    .heightIn(min = 56.dp, max = 128.dp)
                    .testTag(MESSAGE_BOX_TAG),
                messageText = messageText,
                enabled = hasWritePermissions,
                onMessageTextChange = { messageText = it },
            )
            val imageResource3 = painterResource((R.drawable.send_icon))
            if (isSendIconVisible) {
                IconButton(
                    modifier = Modifier.testTag(SEND_BUTTON_TAG),
                    onClick = {
                        onSendClick(messageText)
                        messageText = ""
                    },
                ) {
                    Image(
                        modifier = Modifier
                            .size(35.dp),
                        painter = imageResource3,
                        contentDescription = "Send message",
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelMessageViewLayoutPreview() {
    ChIMPTheme {
        ChannelMessageView(
            onBackClick = {},
            channel = FakeData.channels[0],
            onChannelNameClick = {},
            onSendClick = {},
            hasWritePermissions = false,
            authenticatedUser = AuthenticatedUser(
                "",
                User(
                    1,
                    "username",
                    "display_name"
                ),
            )
        )
    }
}