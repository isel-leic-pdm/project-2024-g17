package com.leic52dg17.chimp.domain.model

import com.leic52dg17.chimp.domain.model.channel.UserChannel
import org.junit.Test
import java.math.BigInteger

class UserChannelModelTests {
    @Test
    fun can_instantiate_a_valid_user_channel() {
        UserChannel(
            1,
            1,
            VALID_BIG_INT
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_user_id() {
        UserChannel(
            0,
            1,
            VALID_BIG_INT
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_channel_id() {
        UserChannel(
            1,
            0,
            VALID_BIG_INT
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_joined_at() {
        UserChannel(
            1,
            2,
            BigInteger("0")
        )
    }

    companion object {
        val VALID_BIG_INT = BigInteger("1731862334")
    }
}