package com.leic52dg17.chimp.model.user

import com.leic52dg17.chimp.model.common.PermissionLevel

data class UserPermission(
    val user: User,
    val permissionLevel: PermissionLevel
)