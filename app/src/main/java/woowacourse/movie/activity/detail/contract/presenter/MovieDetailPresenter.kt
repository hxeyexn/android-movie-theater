package woowacourse.movie.activity.detail.contract.presenter

import domain.movieinfo.MovieDate
import domain.movieinfo.MovieTime
import domain.screeningschedule.ReservationDate
import woowacourse.movie.activity.detail.contract.MovieDetailContract
import woowacourse.movie.dto.movie.MovieUIModel
import woowacourse.movie.dto.movie.TheaterUIModel
import woowacourse.movie.dto.ticket.TicketCountUIModel
import woowacourse.movie.mapper.movie.mapToUIModel
import woowacourse.movie.mapper.ticket.mapToDomain
import woowacourse.movie.mapper.ticket.mapToUIModel
import java.time.LocalDate

class MovieDetailPresenter(
    private val view: MovieDetailContract.View,
    private var dateSpinnerPosition: Int = 0,
    private var timeSpinnerPosition: Int = 0,
) :
    MovieDetailContract.Presenter {
    private var ticketCount = TicketCountUIModel()

    override fun loadMovieData(data: MovieUIModel) {
        view.setMovieData(data)
    }

    override fun loadDateSpinnerPosition() {
        view.setDateSpinnerPosition(dateSpinnerPosition)
    }

    override fun loadTimeSpinnerPosition() {
        view.setTimeSpinnerPosition(timeSpinnerPosition)
    }

    override fun increaseNumber() {
        val ticketIncrease = ticketCount.mapToDomain().increase()
        ticketCount = ticketIncrease.mapToUIModel()
        view.setBookerNumber(ticketCount)
    }

    override fun decreaseNumber() {
        val ticketDecrease = ticketCount.mapToDomain().decrease()
        ticketCount = ticketDecrease.mapToUIModel()
        view.setBookerNumber(ticketCount)
    }

    override fun onBookBtnClick(
        data: MovieUIModel,
        date: String,
        time: String,
    ) {
        val selectedDate = MovieDate.of(date)
        val selectedTime = MovieTime.of(time)
        view.showSeatSelectPage(
            data,
            ticketCount,
            selectedDate.mapToUIModel(),
            selectedTime.mapToUIModel(),
        )
    }

    override fun loadDateSpinnerData(startDate: LocalDate, endDate: LocalDate) {
        val data = ReservationDate(startDate, endDate).getIntervalDays()
        view.setDateSpinnerData(data)
    }

    override fun loadTimeSpinnerData(movieId: Int, theater: TheaterUIModel) {
        val data = theater.screeningTime[movieId]
        data?.let { view.setTimeSpinnerData(it) }
    }

    override fun saveTimeSpinnerPosition(position: Int) {
        timeSpinnerPosition = position
    }

    override fun getTimeSpinnerPosition(): Int {
        return timeSpinnerPosition
    }

    override fun getDateSpinnerPosition(): Int {
        return dateSpinnerPosition
    }
}
