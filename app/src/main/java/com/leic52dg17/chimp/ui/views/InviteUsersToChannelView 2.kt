package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.ui.components.inputs.SearchBar

@Composable
fun InviteUsersToChannelView(
    channel: Channel,
    users: List<User>,
    onBackClick: () -> Unit,
    onInviteUserClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchValue by remember { mutableStateOf("") }
    var filteredUsers by remember { mutableStateOf(users) }

    fun searchUsers(input: String) {
        searchValue = input
        filteredUsers = users.filter { it.displayName.contains(searchValue, ignoreCase = true) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize().border(1.dp, Color.Red)
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            SearchBar(
                searchValue = searchValue,
                onValueChange = { searchUsers(it) }
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modif
        ) {

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
        onInviteUserClick = { channelId, userId ->  }
    )
}