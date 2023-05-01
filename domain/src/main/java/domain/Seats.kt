package domain

import java.time.LocalDateTime

class Seats(seats: List<Seat> = emptyList()) {
    private val _seats = seats.toMutableList()
    val seats: List<Seat>
        get() = _seats

    fun add(seat: Seat) {
        _seats.add(seat)
    }

    fun containsSeat(seat: Seat): Boolean = seats.contains(seat)

    fun remove(seat: Seat) {
        _seats.remove(seat)
    }

    fun isPossibleSeatSize(count: Int): Boolean {
        return seats.size < count
    }

    fun checkSeatSizeMatch(count: Int): Boolean {
        return seats.size == count
    }

    fun caculateSeatPrice(dateTime: LocalDateTime): Int {
        return seats.fold(0) { total, price ->
            total + price.applyPolicyPrice(dateTime)
        }
    }
}
