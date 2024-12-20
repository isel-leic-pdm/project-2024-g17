package com.leic52dg17.chimp.http.services.registration_invitation.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateRegistrationInvitationResponse (
    val inviteId: Int
)
