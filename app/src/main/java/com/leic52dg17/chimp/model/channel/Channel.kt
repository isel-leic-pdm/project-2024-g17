package com.leic52dg17.chimp.model.channel

import com.leic52dg17.chimp.model.common.ImageResource

data class Channel(
    val displayName: String,
    val latestMessage: String,
    val channelIcon: ImageResource
)