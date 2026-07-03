package com.deference.inventra.core.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.formatToString(
    dateFormat: DateTimeFormat<LocalDateTime> = LocalDateTime.Format {
        day()
        char('-')
        monthNumber()
        char('-')
        year()
    }
): String {
    return this.format(dateFormat)
}