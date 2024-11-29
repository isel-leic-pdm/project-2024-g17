package com.leic52dg17.chimp.http.services.channel.responses.helper_classes

import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import kotlinx.serialization.Serializable

@Serializable
data class GetUserChannelResponse(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val isPrivate: Boolean,
    val permissionLevel: PermissionLevel
)