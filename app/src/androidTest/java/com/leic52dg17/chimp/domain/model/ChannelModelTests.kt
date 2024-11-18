package com.leic52dg17.chimp.domain.model

import com.leic52dg17.chimp.domain.model.channel.Channel
import org.junit.Test

class ChannelModelTests {
    @Test
    fun can_instantiate_a_valid_channel() {
        Channel(
            1,
            VALID_DISPLAY_NAME,
            1,
            emptyList(),
            emptyList(),
            true,
            VALID_ICON_URL
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_blank_display_name() {
        Channel(
            1,
            BLANK,
            1,
            emptyList(),
            emptyList(),
            true,
            VALID_ICON_URL
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_empty_display_name() {
        Channel(
            1,
            EMPTY,
            1,
            emptyList(),
            emptyList(),
            true,
            VALID_ICON_URL
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_channel_id() {
        Channel(
            0,
            VALID_DISPLAY_NAME,
            1,
            emptyList(),
            emptyList(),
            true,
            VALID_ICON_URL
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_owner_id() {
        Channel(
            1,
            VALID_DISPLAY_NAME,
            0,
            emptyList(),
            emptyList(),
            true,
            VALID_ICON_URL
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_because_display_name_is_too_long() {
        Channel(
            1,
            TOO_LONG,
            1,
            emptyList(),
            emptyList(),
            true,
            VALID_ICON_URL
        )
    }

    companion object {
        const val VALID_DISPLAY_NAME = "Display"
        const val VALID_ICON_URL = "Display"
        const val TOO_LONG = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        const val BLANK = "  "
        const val EMPTY = ""
    }
}