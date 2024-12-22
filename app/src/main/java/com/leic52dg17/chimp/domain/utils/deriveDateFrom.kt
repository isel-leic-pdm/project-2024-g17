package com.leic52dg17.chimp.domain.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.deriveDateFrom(): String {
    val instant = Instant.ofEpochSecond(this)
    val localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM")
    return localDate.format(formatter)
}