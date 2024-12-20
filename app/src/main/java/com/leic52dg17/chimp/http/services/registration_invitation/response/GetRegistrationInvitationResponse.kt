package com.leic52dg17.chimp.http.services.registration_invitation.response

import kotlinx.serialization.Serializable

@Serializable
data class GetRegistrationInvitationResponse(
    val id : Int,
    val creatorId : Int,
    val token : String,
    val createdAt : Int,
    val isValid : Boolean,
    val invitationUrl : String
)