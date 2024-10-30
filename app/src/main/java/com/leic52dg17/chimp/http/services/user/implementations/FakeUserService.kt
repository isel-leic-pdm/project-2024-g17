package com.leic52dg17.chimp.http.services.user.implementations

import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.model.user.User

class FakeUserService : IUserService {
    override fun getUserById(id: Int): User? {
        return User(
            1,
            "test username",
            "test display name"
        )
    }
}