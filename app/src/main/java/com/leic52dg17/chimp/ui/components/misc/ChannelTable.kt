package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder

@Composable
fun ChannelTable(
    modifier: Modifier = Modifier,
    channels: List<Channel>,
    title: String = "",
    onGoToChannelClick : (Channel) -> Unit = {}
) {
    Column(
        modifier = modifier
    ){
        Text(
            text = title,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(
                        state = rememberScrollState()
                    )
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                for(channel in channels) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                onGoToChannelClick(channel)
                            }
                            .fillMaxWidth()
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(50))
                                .size(40.dp),
                            painter = rememberAsyncImagePainter(channel.channelIconUrl),
                            contentDescription = stringResource(R.string.channel_icon_cd_en)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .bottomBorder(0.5.dp, MaterialTheme.colorScheme.onSecondary),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = channel.displayName)
                            IconButton(
                                onClick = { onGoToChannelClick(channel) }
                            ) {
                                BackButton(
                                    modifier = Modifier
                                        .rotate(90f),
                                    onBackClick = { /*TODO*/ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChannelTablePreview() {
    ChIMPTheme {
        ChannelTable(
            title = "Channels in common",
            modifier = Modifier
                .padding(horizontal = 8.dp),
            channels = listOf(
                Channel(
                    1,
                    "Common channel 1",
                    1,
                    emptyList(),
                    emptyList(),
                    true,
                    "https://picsum.photos/30/30"
                ),
                Channel(
                    1,
                    "Common channel 2",
                    1,
                    emptyList(),
                    emptyList(),
                    true,
                    "https://picsum.photos/30/30"
                )
            )
        )
    }
}