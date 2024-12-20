package com.leic52dg17.chimp.core.cache.common.initializer

import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser

interface ICacheInitializer {
    fun initializeCache(authenticatedUser: AuthenticatedUser?)
}