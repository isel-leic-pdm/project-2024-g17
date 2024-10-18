package com.leic52dg17.chimp.ui.views.create_channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.BackButton
import com.leic52dg17.chimp.ui.components.InputWithSuggestion
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun CreateChannelView(
    onBackClick: () -> Unit = {},
    onChannelNameInfoClick : (String) -> Unit = {}
) {
    var channelNameInputValue by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButton(
            modifier = Modifier
                .padding(vertical = 16.dp),
            onBackClick = onBackClick
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 64.dp),
                text = stringResource(id = R.string.create_channel_title_en),
                textAlign = TextAlign.Center,
                fontFamily = MaterialTheme.typography.displaySmall.fontFamily,
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                fontWeight = MaterialTheme.typography.titleSmall.fontWeight
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.give_a_name_to_channel_en),
                        textAlign = TextAlign.Center,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                    )
                    InputWithSuggestion(
                        value = channelNameInputValue,
                        onInfoClick = { onChannelNameInfoClick(
                            "The channel name must have a maximum of 50 characters.\n" +
                            "It can contain special characters and numbers."
                        ) },
                        onValueChange = {
                            channelNameInputValue = it
                        },
                        inputModifier = Modifier
                            .height(90.dp)
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateChannelViewPreview() {
    ChIMPTheme {
        CreateChannelView()
    }
}