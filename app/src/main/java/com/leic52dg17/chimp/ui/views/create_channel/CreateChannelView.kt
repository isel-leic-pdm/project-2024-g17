package com.leic52dg17.chimp.ui.views.create_channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.inputs.LabelAndInputWithSuggestion
import com.leic52dg17.chimp.ui.components.toggles.ToggleWithLabel
import com.leic52dg17.chimp.ui.screens.main.MainScreenState
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun CreateChannelView(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onBackClick: () -> Unit = {},
    onChannelNameInfoClick : (String) -> Unit = {}
) {
    var channelNameInputValue by rememberSaveable(saver = MainScreenState.StringSaver) {
        mutableStateOf("")
    }

    var isPrivateToggleText by rememberSaveable(saver = MainScreenState.StringSaver) {
        mutableStateOf("Public")
    }

    var isPrivate by rememberSaveable(saver = MainScreenState.BooleanSaver) {
        mutableStateOf(false)
    }


    val channelNameInfoText = stringResource(id = R.string.channel_name_constraints_en)

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButton(
            modifier = Modifier
                .padding(vertical = 16.dp),
            onBackClick = { onBackClick() }
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
                    LabelAndInputWithSuggestion(
                        label = stringResource(id = R.string.give_a_name_to_channel_en),
                        inputValue = channelNameInputValue,
                        textModifier = Modifier
                            .padding(horizontal = 16.dp),
                        inputModifier = Modifier
                            .height(90.dp)
                            .fillMaxWidth()
                            .padding(16.dp),
                        onInfoClick = {
                            onChannelNameInfoClick(channelNameInfoText)
                        },
                        onValueChange = {
                            channelNameInputValue = it
                        }
                    )
                    ToggleWithLabel(
                        text = isPrivateToggleText,
                        checked = isPrivate,
                        onCheckedChange = {
                            isPrivateToggleText = if (it) {
                                isPrivate = true
                                "Private"
                            } else {
                                isPrivate = false
                                "Public"
                            }
                        }
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