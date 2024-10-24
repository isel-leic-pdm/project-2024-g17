package com.leic52dg17.chimp.http.services.channel.results

import com.leic52dg17.chimp.model.common.Either
import com.leic52dg17.chimp.model.common.Error

sealed class ChannelCreationError: Error {
    data class UserCouldNotBeFound(override val message: String?) : ChannelCreationError()
}

typealias ChannelCreationResult = Either<Error, Int>