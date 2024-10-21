package com.leic52dg17.chimp.ui.views.create_channel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.inputs.LabelAndInputWithSuggestion
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.components.misc.UserPool
import com.leic52dg17.chimp.ui.components.toggles.ToggleWithLabel
import com.leic52dg17.chimp.ui.screens.main.MainScreenState
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun CreateChannelView(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onBackClick: () -> Unit = {},
    onChannelNameInfoClick: (String) -> Unit = {}
) {
    var channelNameInputValue by rememberSaveable(saver = MainScreenState.StringSaver) {
        mutableStateOf("")
    }

    var userSearchInputValue by rememberSaveable(saver = MainScreenState.StringSaver) {
        mutableStateOf("")
    }

    var isPrivateToggleText by rememberSaveable(saver = MainScreenState.StringSaver) {
        mutableStateOf("Public")
    }

    var isPrivate by rememberSaveable(saver = MainScreenState.BooleanSaver) {
        mutableStateOf(false)
    }

    val privateText = stringResource(id = R.string.visibility_private_en)
    val publicText = stringResource(id = R.string.visibility_public_en)
    val channelNameInfoText = stringResource(id = R.string.channel_name_constraints_en)

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButton(
            modifier = Modifier.padding(vertical = 8.dp),
            onBackClick = { onBackClick() }
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 32.dp),
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
                        textModifier = Modifier.padding(horizontal = 16.dp),
                        inputModifier = Modifier
                            .height(90.dp)
                            .fillMaxWidth()
                            .padding(16.dp),
                        onInfoClick = { onChannelNameInfoClick(channelNameInfoText) },
                        onValueChange = { channelNameInputValue = it },
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily!!,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight!!
                    )
                    ToggleWithLabel(
                        text = isPrivateToggleText,
                        checked = isPrivate,
                        onCheckedChange = {
                            isPrivateToggleText = if (it) {
                                isPrivate = true
                                privateText
                            } else {
                                isPrivate = false
                                publicText
                            }
                        }
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                            text = "Add Users to the channel"
                        )
                        SearchBar(
                            onValueChange = { userSearchInputValue = it },
                            searchValue = userSearchInputValue,
                        )
                        Box(
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            UserPool(
                                boxModifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .height(300.dp)
                            )
                        }
                    }
                }
            }
        }
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Create")
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