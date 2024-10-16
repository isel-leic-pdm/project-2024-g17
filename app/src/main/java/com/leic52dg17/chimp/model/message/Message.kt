package com.leic52dg17.chimp.model.message

import java.math.BigInteger

data class Message(
    val userId: Int,
    val channelId : Int,
    val text: Char,
    val createdAt : BigInteger,
)