package com.leic52dg17.chimp.http.services.auth.results

import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.model.common.Either
import com.leic52dg17.chimp.model.common.Error

sealed class UserSignUpError : Error {
    data class AuthenticationError(override val message: String?): UserSignUpError()
}

typealias UserSignUpResult = Either<UserSignUpError, AuthenticatedUser>