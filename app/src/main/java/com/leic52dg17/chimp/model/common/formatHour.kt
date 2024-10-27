package com.leic52dg17.chimp.model.common

import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.Date

fun formatHours(timestamp: BigInteger): String {
    val millis = timestamp.toLong() * 1000
    val date = Date(millis)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}