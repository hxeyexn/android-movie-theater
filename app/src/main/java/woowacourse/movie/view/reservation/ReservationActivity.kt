package woowacourse.movie.view.reservation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import woowacourse.movie.R
import woowacourse.movie.databinding.ActivityReservationBinding
import woowacourse.movie.util.DATE_FORMATTER
import woowacourse.movie.util.getParcelableCompat
import woowacourse.movie.view.model.MovieListModel.MovieUiModel
import woowacourse.movie.view.model.MovieTheater
import woowacourse.movie.view.model.ReservationOptions
import woowacourse.movie.view.seatselection.SeatSelectionActivity
import java.time.LocalDate
import java.time.LocalTime

class ReservationActivity : AppCompatActivity(), ReservationContract.View {

    private lateinit var binding: ActivityReservationBinding
    override lateinit var presenter: ReservationContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation)
        binding.movie = getMovie()
        binding.movieTheater = intent.getParcelableCompat<MovieTheater>(MOVIE_THEATER)
        setContentView(binding.root)

        val reservationPresenter = ReservationPresenter(this, getMovie())
        presenter = reservationPresenter
        binding.presenter = reservationPresenter

        presenter.setUpScreeningDateTime()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.setPeopleCount()
    }

    private fun getMovie(): MovieUiModel {
        val movie = intent.getParcelableCompat<MovieUiModel>(MOVIE)
        requireNotNull(movie) { "인텐트로 받아온 데이터가 널일 수 없습니다." }
        return movie
    }

    override fun initMovieView(movie: MovieUiModel) {
        binding.apply {
            moviePoster.setImageResource(movie.posterResourceId)
            movieScreeningDate.text = getString(R.string.screening_date_format).format(
                movie.screeningStartDate.format(DATE_FORMATTER),
                movie.screeningEndDate.format(DATE_FORMATTER)
            )
            movieRunningTime.text =
                getString(R.string.running_time_format).format(movie.runningTime)
        }
    }

    override fun setUpDateSpinner(
        screeningDates: List<LocalDate>
    ) {
        val dateSpinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            screeningDates
        )

        binding.dateSpinner.apply {
            adapter = dateSpinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.selectScreeningDate(
                        screeningDates[position],
                        intent.getParcelableCompat<MovieTheater>(MOVIE_THEATER)?.screeningTimeslot
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    override fun setUpTimeSpinner(screeningTimes: List<LocalTime>, selectedPosition: Int?) {
        val timeSpinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            screeningTimes
        )
        binding.timeSpinner.apply {
            adapter = timeSpinnerAdapter

            if (selectedPosition != null) {
                this.setSelection(selectedPosition, false)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.selectScreeningTime(screeningTimes[position], position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    override fun setPeopleCountText(count: Int) {
        binding.peopleCount.text = count.toString()
    }

    override fun toSeatSelectionScreen(
        reservationOptions: ReservationOptions,
        movie: MovieUiModel
    ) {
        startActivity(SeatSelectionActivity.newIntent(this, reservationOptions, movie))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val movieTheater = intent.getParcelableCompat<MovieTheater>(MOVIE_THEATER)
        outState.apply {
            movieTheater?.let {
                putParcelable(
                    RESERVATION_OPTIONS, presenter.getReservationOptions(it.name)
                )
            }
            putInt(SELECTED_TIME_POSITION, binding.timeSpinner.selectedItemPosition)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        presenter.saveTimePosition(savedInstanceState.getInt(SELECTED_TIME_POSITION))

        savedInstanceState.getParcelableCompat<ReservationOptions>(RESERVATION_OPTIONS)?.let {
            presenter.restoreReservationOptions(
                it.screeningDateTime.toLocalDate(),
                it.screeningDateTime.toLocalTime(),
                it.peopleCount
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val SELECTED_TIME_POSITION = "SELECTED_TIME_POSITION"
        private const val RESERVATION_OPTIONS = "RESERVATION_OPTIONS"
        private const val MOVIE = "MOVIE"
        private const val MOVIE_THEATER = "MOVIE_THEATER"
        fun newIntent(context: Context, movie: MovieUiModel, movieTheater: MovieTheater): Intent {
            val intent = Intent(context, ReservationActivity::class.java)
            intent.putExtra(MOVIE, movie)
            intent.putExtra(MOVIE_THEATER, movieTheater)
            return intent
        }
    }
}
