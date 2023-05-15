package woowacourse

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.app.presentation.model.BookedMovie
import woowacourse.app.presentation.ui.seat.SeatContract
import woowacourse.app.presentation.ui.seat.SeatPresenter
import woowacourse.domain.BoxOffice
import woowacourse.domain.movie.Movie
import woowacourse.domain.movie.ScreeningPeriod
import woowacourse.domain.reservation.ReservationRepository
import woowacourse.domain.theater.ScreeningMovie
import woowacourse.domain.theater.SeatStructure
import woowacourse.domain.theater.Theater
import woowacourse.domain.theater.TheaterRepository
import woowacourse.domain.ticket.Position
import woowacourse.domain.ticket.Seat
import woowacourse.domain.ticket.SeatRank
import woowacourse.domain.util.CgvResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class SeatPresenterTest {
    private lateinit var presenter: SeatContract.Presenter
    private lateinit var view: SeatContract.View
    private lateinit var bookedMovie: BookedMovie
    private lateinit var boxOffice: BoxOffice
    private lateinit var theaterRepository: TheaterRepository
    private val seat = Seat(position = Position(2, 2), rank = SeatRank.S)

    @Before
    fun setUp() {
        view = mockk<SeatContract.View>(relaxed = true)
        setUpTheaterRepository()
        setUpBoxOffice()
        bookedMovie = BookedMovie(
            movie = Movie(
                1,
                "해리 포터",
                ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 4, 10)),
                120,
                "재밌음",
            ),
            bookedDateTime = LocalDateTime.of(2024, 3, 2, 10, 0),
            theaterId = THEATER_ID,
            ticketCount = 2,
        )
        presenter = SeatPresenter(
            bookedMovie = bookedMovie,
            view = view,
            boxOffice = boxOffice,
            theaterRepository = theaterRepository,
        )
    }

    fun setUpTheaterRepository() {
        theaterRepository = mockk()
        val theater = Theater(
            id = THEATER_ID,
            name = "선릉",
            screeningMovies = listOf(
                ScreeningMovie(
                    movie = Movie(
                        id = 1,
                        title = "스즈메의 문단속",
                        screeningPeriod = ScreeningPeriod(
                            LocalDate.of(2024, 3, 1),
                            LocalDate.of(2024, 3, 1),
                        ),
                        runningTime = 130,
                        description = "문 잘 잠구는 공포영화",
                    ),
                    times = listOf(LocalTime.of(10, 0)),
                ),
            ),
            seatStructure = SeatStructure(
                rowSize = 5,
                columnSize = 4,
                sRankRange = listOf(2..3),
                aRankRange = listOf(4..4),
                bRankRange = listOf(0..1),
            ),
        )
        every { theaterRepository.getTheater(THEATER_ID) } returns CgvResult.Success(theater)
    }

    fun setUpBoxOffice() {
        val reservationRepository: ReservationRepository = mockk()
        boxOffice = BoxOffice(reservationRepository)
    }

    @Test
    fun `좌석을 티켓 수만큼 선택하지 않은 상황에서 좌석을 누르면 좌석이 선택된다`() {
        // given

        // when
        presenter.selectSeat(seat)

        // then
        assertEquals(1, presenter.getSelectedSeats().size)
        verify(exactly = 1) { view.selectSeatView(seat) }
        verify(exactly = 0) { view.showSeatFull() }
    }

    companion object {
        const val THEATER_ID: Long = 0
    }
}
