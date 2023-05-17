package woowacourse.movie.model

import com.woowacourse.domain.movie.MovieBookingSeatInfo
import java.io.Serializable

fun MovieBookingSeatInfoUIModel.toDomain() =
    MovieBookingSeatInfo(movieBookingInfo.toDomain(), seats, totalPrice)

fun MovieBookingSeatInfo.toPresentation() =
    MovieBookingSeatInfoUIModel(movieBookingInfo.toPresentation(), seats, totalPrice)

fun MovieBookingSeatInfoUIModel.toHistoryData() = BookingHistoryData(
    movieBookingInfo.theaterMovie.movieInfo.movie.title,
    movieBookingInfo.formatBookHistoryDate(),
    movieBookingInfo.ticketCount,
    seats,
    totalPrice,
    movieBookingInfo.theaterMovie.name,
)

data class MovieBookingSeatInfoUIModel(
    val movieBookingInfo: MovieBookingInfoUiModel,
    val seats: List<String>,
    val totalPrice: String,
) : Serializable, TicketData {
    companion object {
        val dummyData = MovieBookingSeatInfoUIModel(
            MovieBookingInfoUiModel.dummyData,
            emptyList(),
            "",
        )
    }
}
