package com.leic52dg17.chimp.domain.model

import com.leic52dg17.chimp.domain.model.user.User
import org.junit.Test

class UserModelTests {
    @Test
    fun can_instantiate_valid_user() {
        User(
            1,
            USERNAME,
            DISPLAY_NAME
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_user_id() {
        User(
            0,
            USERNAME,
            DISPLAY_NAME
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_empty_username() {
        User(
            1,
            EMPTY,
            DISPLAY_NAME
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_blank_username() {
        User(
            1,
            BLANK,
            DISPLAY_NAME
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_too_long_username() {
        User(
            1,
            USERNAME_TOO_LONG,
            DISPLAY_NAME
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_empty_display_name() {
        User(
            1,
            USERNAME,
            EMPTY
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_blank_display_name() {
        User(
            1,
            USERNAME,
            BLANK
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_too_long_display_name() {
        User(
            1,
            USERNAME,
            DISPLAY_TOO_LONG
        )
    }

    companion object {
        const val USERNAME = "username"
        const val DISPLAY_NAME = "Display Name"
        const val BLANK = " "
        const val EMPTY = ""
        const val USERNAME_TOO_LONG = "Username_12221312312131231312312312312312312312312312312312312312312312312312312312312312"
        const val DISPLAY_TOO_LONG = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    }
}