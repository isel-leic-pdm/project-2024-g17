package com.leic52dg17.chimp.http.services.user.implementations

import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.domain.model.user.User

class FakeUserService : IUserService {
    override suspend fun getUserById(id: Int): User? {
        return FakeData.users.firstOrNull { it.id == id }
    }

    override suspend fun getAllUsers(username: String?, page: Int?, limit: Int?): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelUsers(channelId: Int): List<User> {
        TODO("Not yet implemented")
    }
}