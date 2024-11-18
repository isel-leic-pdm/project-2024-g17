package com.leic52dg17.chimp.http.services.channel.results

import com.leic52dg17.chimp.domain.model.common.Either
import com.leic52dg17.chimp.domain.model.common.Error

class ChannelUpdateError(override val message: String?): Error

typealias RemoveUserFromChannelResult = Either<ChannelUpdateError, Int>