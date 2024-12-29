package com.leic52dg17.chimp.ui.views.user_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.buttons.BackButton

const val USER_PROFILE_PICTURE_TAG = "user_profile_picture"
const val USER_DISPLAY_NAME_TAG = "user_display_name"
const val USER_USERNAME_TAG = "user_username"
const val LOGOUT_BUTTON_TAG = "logout_button"
const val CHANGE_PASSWORD_BUTTON_TAG = "change_password_button"
const val REGISTRATION_INVITATION_BUTTON_TAG = "registration_invitation_button"
const val CHANNEL_INVITATIONS_BUTTON_TAG = "channel_invitations_button"

@Composable
fun UserInfoView(
    user: User,
    isCurrentUser: Boolean,
    onBackClick: () -> Unit,
    onInvitationsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onRegistrationInvitationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
        ) {
            val arrangement =
                if (isCurrentUser) Arrangement.End
                else Arrangement.SpaceBetween
            Row(
                horizontalArrangement = arrangement,
                modifier = modifier.fillMaxWidth(),
            ) {
                if (!isCurrentUser) {
                    BackButton(
                        onBackClick = onBackClick
                    )
                }
                if (isCurrentUser) {
                    IconButton(
                        modifier = Modifier.testTag(CHANNEL_INVITATIONS_BUTTON_TAG),
                        onClick = onInvitationsClick,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MailOutline,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = stringResource(R.string.back_button_text_cd)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(top = 32.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://picsum.photos/300/300"),
                    contentDescription = stringResource(R.string.user_profile_picture_cd_en),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .shadow(8.dp, RoundedCornerShape(10))
                        .clip(RoundedCornerShape(10))
                        .size(256.dp)
                        .testTag(USER_PROFILE_PICTURE_TAG)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(top = 10.dp)
                ) {
                    Text(
                        text = user.displayName,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .testTag(USER_DISPLAY_NAME_TAG)
                    )
                    Text(
                        text = "@" + user.username,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        modifier = Modifier.testTag(USER_USERNAME_TAG)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                if (isCurrentUser) {
                    Button(
                        onClick = onLogoutClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(20),
                        modifier = modifier
                            .width(100.dp)
                            .testTag(LOGOUT_BUTTON_TAG)
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
                        modifier = modifier
                            .width(170.dp)
                            .testTag(CHANGE_PASSWORD_BUTTON_TAG)
                    ) {
                        Text(stringResource(R.string.change_password_text_en))
                    }
                    Button(
                        onClick = onRegistrationInvitationClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(20),
                        modifier = modifier
                            .width(250.dp)
                            .testTag(REGISTRATION_INVITATION_BUTTON_TAG)
                    ) {
                        Text("Invite a friend to the App!")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserInfoViewPreview() {
    UserInfoView(
        user = User(1, "username", "Harvyyyy"),
        isCurrentUser = true,
        onBackClick = { },
        onInvitationsClick = { },
        onLogoutClick = { },
        onChangePasswordClick = { },
        onRegistrationInvitationClick = { }
    )
}