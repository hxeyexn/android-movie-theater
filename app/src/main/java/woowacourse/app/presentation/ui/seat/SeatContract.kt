package woowacourse.app.presentation.ui.seat

import woowacourse.domain.movie.Movie
import woowacourse.domain.reservation.Reservation
import woowacourse.domain.ticket.Position
import woowacourse.domain.ticket.Seat

interface SeatContract {
    interface View {
        val presenter: Presenter
        fun setTableSize(rowSize: Int, columnSize: Int)
        fun setTableColor(
            sRank: List<IntRange>,
            aRank: List<IntRange>,
            bRank: List<IntRange>,
        )

        fun setTableClickListener(getSeat: (clickedPosition: Position) -> Seat)
        fun showSeatFull()
        fun selectSeatView(seat: Seat)
        fun setPaymentText(payment: Int)
        fun setConfirmButtonEnable(isSeatFull: Boolean)
        fun completeBooking(reservation: Reservation)
        fun showNoSuchTheater()
        fun showBookingConfirmDialog()
    }

    interface Presenter {
        val movie: Movie
        fun getSelectedSeats(): Set<Seat>
        fun selectSeat(seat: Seat)
        fun setPayment()
        fun setTable()
        fun completeBooking()
        fun clickConfirmButton()
    }
}
