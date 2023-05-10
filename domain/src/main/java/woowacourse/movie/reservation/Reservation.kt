package woowacourse.movie.reservation

import woowacourse.movie.PaymentType
import woowacourse.movie.ticket.Ticket
import java.time.LocalDateTime

data class Reservation(
    val theaterId: Long,
    val tickets: Set<Ticket>,
    val paymentType: PaymentType = PaymentType.OFFLINE,
) {
    val payment: Int = tickets.sumOf { it.price }
    val movieId: Long get() = tickets.first().movieId
    val bookedDateTime: LocalDateTime get() = tickets.first().bookedDateTime
    val count: Int get() = tickets.size
}
