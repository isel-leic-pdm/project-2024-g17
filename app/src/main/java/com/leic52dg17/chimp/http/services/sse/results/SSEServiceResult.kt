package com.leic52dg17.chimp.http.services.sse.results

import com.leic52dg17.chimp.domain.model.common.Either
import com.leic52dg17.chimp.domain.model.common.Error

class SSEServiceError(override val message: String?): Error

typealias SSEServiceResult = Either<SSEServiceError, Unit>