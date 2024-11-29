package com.leic52dg17.chimp.domain.model.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PermissionLevelSerializer::class)
enum class PermissionLevel {
    RW,
    RR
}

object PermissionLevelSerializer : KSerializer<PermissionLevel> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PermissionLevel", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: PermissionLevel) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): PermissionLevel {
        return when (val value = decoder.decodeString().lowercase()) {
            "rw" -> PermissionLevel.RW
            "rr" -> PermissionLevel.RR
            else -> throw IllegalArgumentException("Unknown PermissionLevel: $value")
        }
    }
}