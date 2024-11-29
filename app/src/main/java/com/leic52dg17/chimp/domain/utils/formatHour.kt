package com.leic52dg17.chimp.domain.utils

import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.Date

fun formatHours(timestamp: Long): String {
    val millis = timestamp * 1000
    val date = Date(millis)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}