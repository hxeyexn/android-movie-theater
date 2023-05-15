package woowacourse.app.presentation.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDate.formatScreenDate(): String {
    return format(DateTimeFormatter.ofPattern("YYYY.MM.dd"))
}

fun LocalDateTime.formatScreenDateTime(): String {
    return format(DateTimeFormatter.ofPattern("YYYY.MM.dd HH:mm"))
}

fun LocalDateTime.formatBookedDateTime(): String {
    return format(DateTimeFormatter.ofPattern("YYYY.MM.dd | HH:mm"))
}
