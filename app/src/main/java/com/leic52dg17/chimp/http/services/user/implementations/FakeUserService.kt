package com.leic52dg17.chimp.http.services.user.implementations

import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.model.user.User

class FakeUserService : IUserService {
    override fun getUserById(id: Int): User? {
        return FakeData.users.firstOrNull { it.userId == id }
    }

    override fun getAllUsers(): List<User> {
        return FakeData.users
    }
}