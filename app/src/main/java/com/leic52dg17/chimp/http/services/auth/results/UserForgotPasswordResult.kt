package com.leic52dg17.chimp.http.services.auth.results

import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.common.Either
import com.leic52dg17.chimp.domain.model.common.Error

sealed class UserForgotPasswordResultError : Error {
    data class AuthenticationError(override val message: String?): UserForgotPasswordResultError()
}

typealias UserForgotPasswordResult = Either<UserForgotPasswordResultError, AuthenticatedUser>