package com.leic52dg17.chimp.http.services.channel.responses

import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import kotlinx.serialization.Serializable

@Serializable
data class GetUserPermissionsResponse(
    val userId: Int,
    val channelId: Int,
    val permissionLevel: PermissionLevel
)
