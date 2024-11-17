package com.leic52dg17.chimp.domain.model

import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import org.junit.Test
import java.util.UUID

class ChannelInvitationModelTests {
    @Test
    fun can_instantiate_a_valid_channel_invitation() {
        ChannelInvitation(
            RANDOM_UUID,
            1,
            1,
            2,
            PermissionLevel.RR
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_channel_id() {
        ChannelInvitation(
            RANDOM_UUID,
            0,
            1,
            2,
            PermissionLevel.RR
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_sender_id() {
        ChannelInvitation(
            RANDOM_UUID,
            1,
            0,
            2,
            PermissionLevel.RR
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_receiver_id() {
        ChannelInvitation(
            RANDOM_UUID,
            1,
            1,
            0,
            PermissionLevel.RR
        )
    }

    companion object {
        val RANDOM_UUID: UUID = UUID.randomUUID()
    }
}