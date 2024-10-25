package com.leic52dg17.chimp.model.user

import java.util.UUID

data class UserRequest(
    val requestId: Int,
    val userId: Int,
    val invitationCode: UUID
)