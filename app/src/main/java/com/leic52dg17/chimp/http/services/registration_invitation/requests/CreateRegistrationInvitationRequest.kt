package com.leic52dg17.chimp.http.services.registration_invitation.requests

import kotlinx.serialization.Serializable


@Serializable
data class CreateRegistrationInvitationRequest (
    val creatorId: Int
)