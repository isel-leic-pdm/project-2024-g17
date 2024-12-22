package com.leic52dg17.chimp.ui.views.public_channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.components.misc.ChannelCard
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun PublicChannelsView(
    currentSearchValue: String,
    onValueChange: (String) -> Unit,
    onChannelJoin: (Channel) -> Unit,
    onBackClick: () -> Unit,
    channels: List<Channel>,
    currentPage: Int,
    onPageChange: (Int, String) -> Unit
) {
    var searchValue by remember {
        mutableStateOf(TextFieldValue(currentSearchValue))
    }

    var isDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    var currentChannelName by rememberSaveable {
        mutableStateOf("")
    }

    var currentChannel by rememberSaveable {
        mutableStateOf<Channel?>(null)
    }

    fun onChannelClick(channel: Channel) {
        currentChannelName = channel.displayName
        currentChannel = channel
        isDialogOpen = true
    }

    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        searchValue = searchValue.copy(selection = TextRange(searchValue.text.length))
    }

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = {
                isDialogOpen = false
                currentChannelName = ""
                currentChannel = null
            }
        ) {
            Surface(
                modifier = Modifier
                    .size(272.dp)
                    .clip(RoundedCornerShape(10))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        text = currentChannelName
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.join_confirmation_text_en)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            onClick = {
                                currentChannel?.let { channel ->
                                    onChannelJoin(channel)
                                }
                            }
                        ) {
                            Text(text = "Yes")
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            onClick = {
                                isDialogOpen = false
                                currentChannelName = ""
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        ) {
            BackButton(onBackClick = { onBackClick() })
        }
        Spacer(modifier = Modifier.height(64.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                text = stringResource(id = R.string.public_channels_title_en)
            )
            PublicChannelsSearchBar(
                textFieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
                    .focusRequester(focusRequester),
                onValueChange = { value ->
                    searchValue = value
                    onValueChange(value.text)
                },
                searchValue = searchValue
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (channels.isNotEmpty()) {
                        for (channel in channels) {
                            ChannelCard(channel = channel) {
                                onChannelClick(channel)
                            }
                        }
                    } else {
                        Text(text = "No channels to display")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    BackButton(
                        enabled = currentPage > 0,
                        onBackClick = { onPageChange(currentPage - 1, searchValue.text) }
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = currentPage.toString()
                    )
                    BackButton(
                        enabled = channels.isNotEmpty(),
                        modifier = Modifier
                            .rotate(90f),
                        onBackClick = { onPageChange(currentPage + 1, searchValue.text) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PublicChannelsPreview() {
    ChIMPTheme {
        PublicChannelsView(
            "",
            {},
            {},
            {},
            FakeData.channels,
            0,
            {page, name ->}
        )
    }
}