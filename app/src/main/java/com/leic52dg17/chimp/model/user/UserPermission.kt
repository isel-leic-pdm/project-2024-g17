package com.leic52dg17.chimp.model.user

import com.leic52dg17.chimp.model.common.PermissionLevels

data class UserPermission(
    val user: User,
    val permissionLevel: PermissionLevels
)