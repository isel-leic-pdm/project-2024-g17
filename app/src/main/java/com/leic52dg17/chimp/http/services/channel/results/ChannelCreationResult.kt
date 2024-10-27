package com.leic52dg17.chimp.http.services.channel.results

import com.leic52dg17.chimp.model.common.Either
import com.leic52dg17.chimp.model.common.Error

class ChannelCreationError(override val message: String?): Error

typealias ChannelCreationResult = Either<ChannelCreationError, Int>