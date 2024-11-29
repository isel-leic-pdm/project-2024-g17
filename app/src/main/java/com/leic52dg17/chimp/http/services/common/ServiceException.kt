package com.leic52dg17.chimp.http.services.common

class ServiceException(override val message: String?, val type: ServiceErrorTypes): Exception()