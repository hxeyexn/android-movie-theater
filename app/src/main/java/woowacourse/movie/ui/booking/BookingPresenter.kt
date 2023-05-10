package woowacourse.movie.ui.booking

import woowacourse.movie.model.BookedMovie
import woowacourse.movie.model.main.MainModelRepository
import woowacourse.movie.model.main.MovieMapper.toUiModel
import woowacourse.movie.movie.Movie
import woowacourse.movie.theater.Theater
import woowacourse.movie.ticket.TicketCount

class BookingPresenter(
    private val view: BookingContract.View,
    repository: MainModelRepository,
    movieId: Long,
    theaterId: Long
) : BookingContract.Presenter {

    override val movie: Movie = repository.findMovieById(movieId)
    override val theater: Theater = repository.findTheaterById(theaterId)
    override var ticketCount: TicketCount = TicketCount()

    override fun initBookingMovie() {
        view.initView(movie.toUiModel())
        view.setTicketCountText(ticketCount.value)
        view.setDates(movie.screeningDates)
        view.setTimes(theater.screeningTimes)
    }

    override fun minusTicketCount() {
        ticketCount = ticketCount.minus()

        view.setTicketCountText(ticketCount.value)
    }

    override fun plusTicketCount() {
        ticketCount = ticketCount.plus()

        view.setTicketCountText(ticketCount.value)
    }

    override fun onCompletedBookingMovie() {
        val bookedMovie = BookedMovie(
            movieId = movie.id,
            theaterId = 0,
            ticketCount = ticketCount.value,
            bookedDateTime = view.selectedDateTime
        )

        view.navigateToSeatView(
            bookedMovie = bookedMovie
        )
    }
}
