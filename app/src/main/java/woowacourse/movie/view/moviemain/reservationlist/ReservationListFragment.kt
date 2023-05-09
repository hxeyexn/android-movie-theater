package woowacourse.movie.view.moviemain.reservationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import woowacourse.movie.R
import woowacourse.movie.data.MovieMockRepository
import woowacourse.movie.data.ReservationDatabase
import woowacourse.movie.data.SeatDatabase
import woowacourse.movie.data.dbhelper.ReservationDbHelper
import woowacourse.movie.databinding.FragmentReservationListBinding
import woowacourse.movie.view.model.ReservationUiModel
import woowacourse.movie.view.reservationcompleted.ReservationCompletedActivity

class ReservationListFragment :
    Fragment(R.layout.fragment_reservation_list),
    ReservationListContract.View {

    private lateinit var binding: FragmentReservationListBinding
    override lateinit var presenter: ReservationListContract.Presenter
    override fun showReservationList(reservations: List<ReservationUiModel>) {
        binding.recyclerview.adapter = ReservationListAdapter(
            reservations
        ) {
            presenter.finishReservation(it)
        }
    }

    override fun toReservationCompletedScreen(reservation: ReservationUiModel) {
        val intent = ReservationCompletedActivity.newIntent(requireContext(), reservation)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservationListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ReservationListPresenter(
            this,
            ReservationDatabase(
                ReservationDbHelper(requireContext()).writableDatabase,
                SeatDatabase(requireContext()),
                MovieMockRepository
            )
        )
        presenter.loadReservationList()
    }
}
