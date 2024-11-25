package com.leic52dg17.chimp.http.services.auth.results

import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.common.Either
import com.leic52dg17.chimp.domain.model.common.Error

sealed class UserChangePasswordError : Error {
    data class AuthenticationError(override val message: String?): UserChangePasswordError()
}

typealias UserChangePasswordResult = Either<UserChangePasswordError, AuthenticatedUser>