package com.leic52dg17.chimp.http.services.registration_invitation

interface IRegistrationInvitationService {

    suspend fun createRegistrationInvitation(
        creatorId: Int,
    ): String

}