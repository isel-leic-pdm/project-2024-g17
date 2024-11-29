package com.leic52dg17.chimp.http.services.channel.responses

import com.leic52dg17.chimp.http.services.channel.responses.helper_classes.GetUserChannelResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetChannelsResponse(
    val channels: List<GetUserChannelResponse>
)