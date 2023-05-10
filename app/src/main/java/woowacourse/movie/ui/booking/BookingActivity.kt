package woowacourse.movie.ui.booking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import woowacourse.movie.R
import woowacourse.movie.databinding.ActivityBookingBinding
import woowacourse.movie.model.BookedMovie
import woowacourse.movie.model.main.MainModelHandler
import woowacourse.movie.model.main.MovieUiModel
import woowacourse.movie.ui.seat.SeatActivity
import woowacourse.movie.util.formatScreenDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class BookingActivity : AppCompatActivity(), BookingContract.View {

    private lateinit var binding: ActivityBookingBinding
    private val movieId: Long by lazy {
        intent.getLongExtra(MOVIE_ID, -1)
    }
    private val theaterId: Long by lazy {
        intent.getLongExtra(THEATER_ID, 0)
    }
    private val bookingPresenter: BookingContract.Presenter by lazy {
        BookingPresenter(
            view = this,
            repository = MainModelHandler,
            movieId = movieId,
            theaterId = theaterId
        )
    }
    override val selectedDateTime: LocalDateTime
        get() = binding.spinnerDateTime.selectedDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking)
        binding.presenter = bookingPresenter
        bookingPresenter.initBookingMovie()
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onRestoreInstanceState(savedInstanceState)

        setTicketCountText(
            count = savedInstanceState.getInt(TICKET_COUNT)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.run {
            putInt(TICKET_COUNT, binding.textBookingTicketCount.text.toString().toInt())
        }
    }

    override fun initView(movie: MovieUiModel) {
        with(binding) {
            imageBookingPoster.setImageResource(movie.poster)
            textBookingTitle.text = movie.title
            textBookingScreeningDate.text = getString(
                R.string.screening_date,
                movie.startDate.formatScreenDate(),
                movie.endDate.formatScreenDate(),
            )
            textBookingRunningTime.text = getString(R.string.running_time, movie.runningTime)
            textBookingDescription.text = movie.description
        }
        showBackButton()
    }

    override fun setTicketCountText(count: Int) {
        binding.textBookingTicketCount.text = count.toString()
    }

    override fun setTimes(screeningTimes: List<LocalTime>) {
        binding.spinnerDateTime.setTimes(screeningTimes)
    }

    override fun setDates(screeningDates: List<LocalDate>) {
        binding.spinnerDateTime.setDates(screeningDates)
    }

    private fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateToSeatView(bookedMovie: BookedMovie) {
        startActivity(SeatActivity.getIntent(this, bookedMovie))
        finish()
    }

    companion object {
        private const val MOVIE_ID = "MOVIE_ID"
        private const val THEATER_ID = "THEATER_ID"
        private const val TICKET_COUNT = "TICKET_COUNT"

        fun getIntent(
            context: Context,
            movieId: Long,
            theaterId: Long,
        ): Intent {
            return Intent(context, BookingActivity::class.java).apply {
                putExtra(MOVIE_ID, movieId)
                putExtra(THEATER_ID, theaterId)
            }
        }
    }
}
