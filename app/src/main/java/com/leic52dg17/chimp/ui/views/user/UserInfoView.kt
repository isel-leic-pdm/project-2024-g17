package com.leic52dg17.chimp.ui.views.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.misc.ChannelTable

@Composable
fun UserInfoView(
    user: User,
    authenticatedUser: AuthenticatedUser?,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    channelsInCommon: List<Channel> = emptyList(),
    onGoToChannelClick: (Channel) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth(),
        ) {
            BackButton(onBackClick = onBackClick)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(top = 32.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/200/200"),
                contentDescription = stringResource(R.string.user_profile_picture_cd_en),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .padding(bottom = 32.dp)
                    .clip(CircleShape)
                    .size(200.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.displayName,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Text(
                    text = "@" + user.username,
                    modifier = Modifier
                        .alpha(0.5f),
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (authenticatedUser?.user?.userId == user.userId) {
                Button(
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(20),
                ) {
                    Text(stringResource(R.string.logout_en))
                }
                Button(
                    onClick = onChangePasswordClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(20),
                ) {
                    Text(stringResource(R.string.change_password_text_en))
                }
            } else {
                if(channelsInCommon.isNotEmpty()) {
                    ChannelTable(
                        title = "Channels in common with ${user.displayName}",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        channels = channelsInCommon,
                        onGoToChannelClick = {
                            onGoToChannelClick
                        }
                    )
                } else {
                    Text(text = stringResource(id = R.string.no_channels_in_common_en))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserInfoViewPreview() {
    UserInfoView(
        user = User(2, "username", "Harvyyyy"),
        authenticatedUser = AuthenticatedUser(
            authenticationToken = "token",
            user = User(1, "username", "Harvyyyy")
        ),
        onBackClick = { },
        onLogoutClick = { },
        onChangePasswordClick = {},
        channelsInCommon = listOf(
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