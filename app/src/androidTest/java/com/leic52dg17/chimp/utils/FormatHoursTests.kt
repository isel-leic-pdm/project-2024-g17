package com.leic52dg17.chimp.utils

import com.leic52dg17.chimp.domain.utils.formatHours
import org.junit.Test

class FormatHoursTests {
    @Test
    fun returns_expected_format() {
        val formatted = formatHours(TIMESTAMP)
        assert(formatted.length == 5)
        assert(formatted[2] == ':')
    }

    @Test
    fun returns_expected_hours_and_minutes() {
        val formatted = formatHours(TIMESTAMP)
        assert(formatted == "16:52")
    }

    companion object {
        const val TIMESTAMP = 1731862334L
    }
}