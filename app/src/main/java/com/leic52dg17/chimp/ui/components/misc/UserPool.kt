package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

val mockList = listOf(
    User(1, "username1", "User 1"),
    User(2, "username2", "User 2"),
    User(3, "username3", "User 3"),
    User(4, "username4", "User 4"),
    User(5, "username4", "User 5"),
    User(6, "username4", "User 6"),
)

@Composable
fun UserPool(
    users: List<User> = mockList,
    boxModifier: Modifier = Modifier,
) {
    Box(
        modifier = boxModifier
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(
                    state = rememberScrollState()
                )
                .fillMaxWidth()
        ) {
            for(user in users) {
                UserCard(user = user)
            }
        }
    }
}

@Preview
@Composable
fun UserPoolPreview() {
    ChIMPTheme {
        UserPool()
    }
}