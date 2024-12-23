package com.leic52dg17.chimp.http.services.channel.responses

import com.leic52dg17.chimp.http.services.channel.responses.helper_classes.GetPublicChannelResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetPublicChannelsResponse(
    val channels: List<GetPublicChannelResponse> = emptyList()
)
