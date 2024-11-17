package com.leic52dg17.chimp.http.services.message.results

import com.leic52dg17.chimp.domain.model.common.Either
import com.leic52dg17.chimp.domain.model.common.Error
import com.leic52dg17.chimp.domain.model.message.Message

class GetChannelMessagesError(override val message: String?): Error

typealias GetChannelMessagesResult = Either<GetChannelMessagesError, List<Message>?>