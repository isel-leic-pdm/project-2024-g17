package com.leic52dg17.chimp.http.services.user

import com.leic52dg17.chimp.domain.model.user.User

interface IUserService {
    fun getUserById(id: Int): User?
    fun getAllUsers(): List<User>
}