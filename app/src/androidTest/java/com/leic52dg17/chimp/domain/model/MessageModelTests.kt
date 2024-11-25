package com.leic52dg17.chimp.domain.model

import com.leic52dg17.chimp.domain.model.message.Message
import org.junit.Test
import java.math.BigInteger

class MessageModelTests {
    @Test
    fun can_instantiate_valid_message() {
        Message(
            1,
            1,
            1,
            MESSAGE_TEXT,
            TIMESTAMP
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_user_id() {
        Message(
            0,
            1,
            1,
            MESSAGE_TEXT,
            TIMESTAMP
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_channel_id() {
        Message(
            1,
            0,
            1,
            MESSAGE_TEXT,
            TIMESTAMP
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_empty_text() {
        Message(
            1,
            1,
            1,
            EMPTY_TEXT,
            TIMESTAMP
        )
    }

    companion object {
        const val MESSAGE_TEXT = "Test message"
        const val EMPTY_TEXT = ""
        val TIMESTAMP = BigInteger("1731862334")
    }
}