package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder

@Composable
fun InviteUsersToChannelView(
    channel: Channel,
    users: List<User>,
    onBackClick: () -> Unit,
    onInviteUserClick: (Int, Int, PermissionLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchValue by remember { mutableStateOf("") }
    var filteredUsers by remember { mutableStateOf(users) }

    fun searchUsers(input: String) {
        searchValue = input
        filteredUsers = users.filter { user ->
            user.displayName.contains(searchValue, ignoreCase = true) &&
                    channel.users.none { channelUser -> channelUser.userId == user.userId }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            BackButton(onBackClick = onBackClick)
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            SearchBar(
                searchValue = searchValue,
                onValueChange = { searchUsers(it) },
                placeHolderFontSize = MaterialTheme.typography.bodySmall.fontSize,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val toDisplay = filteredUsers

            for (user in toDisplay) {
                var showDropdown by remember { mutableStateOf(false) }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .bottomBorder(0.2.dp, MaterialTheme.colorScheme.secondary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.chimp_blue_final),
                            contentDescription = stringResource(id = R.string.user_icon_cd_en),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                        Text(user.displayName)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showDropdown = true }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.primary,
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Add user to channel"
                            )
                        }
                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    onInviteUserClick(
                                        user.userId,
                                        channel.channelId,
                                        PermissionLevel.RR
                                    )
                                    showDropdown = false
                                },
                                text = { Text("Read Only Permissions") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    onInviteUserClick(
                                        user.userId,
                                        channel.channelId,
                                        PermissionLevel.RW
                                    )
                                    showDropdown = false
                                },
                                text = { Text("Read Write Permissions") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InviteUsersToChannelViewPreview() {
    InviteUsersToChannelView(
        channel = FakeData.channels.first(),
        users = FakeData.users,
        onBackClick = { },
        onInviteUserClick = { channelId, userId, perm -> }
    )
}