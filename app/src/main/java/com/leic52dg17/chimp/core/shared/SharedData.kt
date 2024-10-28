package com.leic52dg17.chimp.core.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.model.user.User

object SharedData {
    private val mockUser = AuthenticatedUser(
        "example_token",
        User(
            1,
            "username1",
            "User 1"
        )
    )

    var authenticatedUser: AuthenticatedUser by mutableStateOf(mockUser)
}