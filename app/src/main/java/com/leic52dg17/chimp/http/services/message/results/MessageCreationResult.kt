package com.leic52dg17.chimp.http.services.message.results

import com.leic52dg17.chimp.domain.model.common.Either
import com.leic52dg17.chimp.domain.model.common.Error

class MessageCreationError(override val message: String?): Error

typealias MessageCreationResult = Either<MessageCreationError, Boolean>