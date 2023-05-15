package woowacourse.app.presentation.ui.seat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import woowacourse.app.data.movie.MovieDao
import woowacourse.app.data.reservation.ReservationDao
import woowacourse.app.data.reservation.ReservationRepositoryImpl
import woowacourse.app.data.reservation.SeatDao
import woowacourse.app.data.theater.MovieTimeDao
import woowacourse.app.data.theater.TheaterDao
import woowacourse.app.data.theater.TheaterRepositoryImpl
import woowacourse.app.presentation.model.BookedMovieUiModel
import woowacourse.app.presentation.model.Mapper.toBookedMovie
import woowacourse.app.presentation.model.Mapper.toUiModel
import woowacourse.app.presentation.model.seat.SeatMapper.toDomainModel
import woowacourse.app.presentation.model.seat.SeatMapper.toUiModel
import woowacourse.app.presentation.model.seat.SeatRankColor
import woowacourse.app.presentation.model.seat.SelectedSeatUiModel
import woowacourse.app.presentation.ui.completed.CompletedActivity
import woowacourse.app.presentation.util.getParcelable
import woowacourse.app.presentation.util.getParcelableBundle
import woowacourse.app.presentation.util.shortToast
import woowacourse.domain.BoxOffice
import woowacourse.domain.reservation.Reservation
import woowacourse.domain.ticket.Position
import woowacourse.domain.ticket.Seat
import woowacourse.movie.R
import woowacourse.movie.databinding.ActivitySeatBinding

class SeatActivity : AppCompatActivity(), SeatContract.View {
    private lateinit var binding: ActivitySeatBinding
    override lateinit var presenter: SeatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPresenter()
        binding.presenter = presenter
        presenter.setTable()
    }

    private fun initPresenter() {
        val bookedMovieUiModel = getData()
        presenter = SeatPresenter(
            this,
            BoxOffice(
                ReservationRepositoryImpl(
                    ReservationDao(this),
                    SeatDao(this),
                    MovieDao(this),
                ),
            ),
            bookedMovieUiModel.toBookedMovie(),
            TheaterRepositoryImpl(
                theaterDataSource = TheaterDao(this),
                movieDataSource = MovieDao(this),
                movieTimeDataSource = MovieTimeDao(this),
            ),
        )
    }

    private fun getData(): BookedMovieUiModel {
        val bookedMovieUiModel = intent.getParcelable(BOOKED_MOVIE, BookedMovieUiModel::class.java)
        if (bookedMovieUiModel == null) {
            shortToast(R.string.error_no_such_data)
            finish()
        }
        return bookedMovieUiModel!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val selectedSeatUiModel =
            SelectedSeatUiModel(presenter.getSelectedSeats().map { it.toUiModel() }.toSet())
        outState.putParcelable("SELECTED_SEAT", selectedSeatUiModel)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedSeatUiModel =
            savedInstanceState.getParcelableBundle("SELECTED_SEAT", SelectedSeatUiModel::class.java)
        restoreSelectedSeat(selectedSeatUiModel)
    }

    private fun restoreSelectedSeat(selectedSeatUiModel: SelectedSeatUiModel) {
        selectedSeatUiModel.selectedSeat.forEach {
            presenter.selectSeat(it.toDomainModel())
        }
    }

    override fun showNoSuchTheater() {
        shortToast(R.string.error_no_such_theater)
        finish()
    }

    override fun showSeatFull() {
        shortToast(R.string.no_more_seat)
    }

    override fun setTableSize(rowSize: Int, columnSize: Int) {
        binding.seatTableLayout.setTable(rowSize, columnSize)
    }

    override fun setTableColor(
        sRank: List<IntRange>,
        aRank: List<IntRange>,
        bRank: List<IntRange>,
    ) {
        binding.seatTableLayout.setColorRange(
            mapOf(
                sRank to SeatRankColor.S.colorId,
                aRank to SeatRankColor.A.colorId,
                bRank to SeatRankColor.B.colorId,
            ),
        )
    }

    override fun setTableClickListener(getSeat: (clickedPosition: Position) -> Seat) {
        binding.seatTableLayout.setClickListener { clickedPosition ->
            val seat = getSeat(clickedPosition)
            presenter.selectSeat(seat)
        }
    }

    override fun selectSeatView(seat: Seat) {
        val view = binding.seatTableLayout[seat.position.row][seat.position.column]
        view.isSelected = !view.isSelected
    }

    override fun setConfirmButtonEnable(isSeatFull: Boolean) {
        val buttonConfirm = binding.buttonSeatConfirm
        buttonConfirm.isEnabled = isSeatFull
        if (buttonConfirm.isEnabled) {
            buttonConfirm.setBackgroundResource(R.color.purple_700)
            return
        }
        buttonConfirm.setBackgroundResource(R.color.gray_400)
    }

    override fun completeBooking(reservation: Reservation) {
        ScreeningTimeReminder(this, reservation.toUiModel())
        startActivity(CompletedActivity.getIntent(this, reservation.toUiModel()))
        finish()
    }

    override fun showBookingConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.booking_confirm)
            .setMessage(R.string.booking_really)
            .setPositiveButton(R.string.yes) { _, _ -> presenter.completeBooking() }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun setPaymentText(payment: Int) {
        binding.textSeatPayment.text = getString(R.string.won, payment)
    }

    companion object {
        private const val BOOKED_MOVIE = "BOOKED_MOVIE"

        fun getIntent(
            context: Context,
            bookedMovieUiModel: BookedMovieUiModel,
        ): Intent {
            return Intent(context, SeatActivity::class.java).apply {
                putExtra(BOOKED_MOVIE, bookedMovieUiModel)
            }
        }
    }
}
