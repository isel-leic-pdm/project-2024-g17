package com.leic52dg17.chimp.model.channel

import java.math.BigInteger

data class UserChannel(
    val userId: Int,
    val channelId: Int,
    val joinedAt: BigInteger
)
