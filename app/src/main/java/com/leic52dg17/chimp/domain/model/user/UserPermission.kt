package com.leic52dg17.chimp.domain.model.user

import com.leic52dg17.chimp.domain.model.common.PermissionLevel

data class UserPermission(
    val user: User,
    val permissionLevel: PermissionLevel
)