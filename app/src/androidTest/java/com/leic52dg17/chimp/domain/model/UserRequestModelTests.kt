package com.leic52dg17.chimp.domain.model

import com.leic52dg17.chimp.domain.model.user.UserRequest
import org.junit.Test
import java.util.UUID

class UserRequestModelTests {
    @Test
    fun can_instantiate_valid_user_request() {
        UserRequest(
            1,
            1,
            UUID.randomUUID()
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_request_id() {
        UserRequest(
            0,
            1,
            UUID.randomUUID()
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun instantiation_fails_with_zero_user_id() {
        UserRequest(
            1,
            0,
            UUID.randomUUID()
        )
    }
}