package com.leic52dg17.chimp.ui.views.create_channel

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.inputs.LabelAndInputWithSuggestion
import com.leic52dg17.chimp.ui.components.toggles.ToggleWithLabel
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun CreateChannelView(
    authenticatedUser: AuthenticatedUser,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onBackClick: () -> Unit = {},
    onChannelNameInfoClick: (String) -> Unit = {},
    onError: (String) -> Unit = {},
    onCreateChannelRequest: (
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ) -> Unit = { _, _, _, _, _ -> }
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var channelNameInputValue by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
        mutableStateOf("")
    }

    var isPrivateToggleText by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
        mutableStateOf("Public")
    }

    var isPrivate by rememberSaveable(saver = MainViewSelectorState.BooleanSaver) {
        mutableStateOf(false)
    }

    val privateText = stringResource(id = R.string.visibility_private_en)
    val publicText = stringResource(id = R.string.visibility_public_en)
    val channelNameInfoText = stringResource(id = R.string.channel_name_constraints_en)
    val userInformationErrorText =
        stringResource(id = R.string.authenticated_user_has_null_values_en)

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
                .fillMaxSize()
                .weight(1f)
                .then(if (isLandscape) Modifier.verticalScroll(rememberScrollState()) else Modifier),
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
                }
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        Log.i("CreateChannelView", "Create channel button clicked")
                        Log.i("CreateChannelView", "Authenticated user: $authenticatedUser")
                        if (authenticatedUser.user == null) {
                            onError(userInformationErrorText)
                            return@Button
                        }
                        Log.i("CreateChannelView", "Creating channel")
                        onCreateChannelRequest(
                            authenticatedUser.user.userId,
                            channelNameInputValue,
                            isPrivate,
                            "",
                            "Channel Icon"
                        )
                    }
                ) {
                    Text(text = "Create")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateChannelViewPreview() {
    ChIMPTheme {
        CreateChannelView(
            AuthenticatedUser()
        )
    }
}