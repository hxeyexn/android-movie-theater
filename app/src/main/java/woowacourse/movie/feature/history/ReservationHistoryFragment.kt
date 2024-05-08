package woowacourse.movie.feature.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import woowacourse.movie.R
import woowacourse.movie.databinding.FragmentReservationHistoryBinding
import woowacourse.movie.feature.history.adapter.ReservationHistoryAdapter
import woowacourse.movie.model.ticket.Ticket
import woowacourse.movie.utils.MovieUtils.makeToast

class ReservationHistoryFragment : Fragment(), ReservationHistoryContract.View {
    private var _binding: FragmentReservationHistoryBinding? = null
    private val binding get() = _binding!!
    private var presenter: ReservationHistoryPresenter = ReservationHistoryPresenter(this)
    private lateinit var reservationHistoryAdapter: ReservationHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_history, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initReservationHistoryRecyclerView()
        presenter.loadTicket()
    }

    override fun showReservationHistory(tickets: List<Ticket>) {
        reservationHistoryAdapter.updateData(tickets)
    }

    private fun initReservationHistoryRecyclerView() {
        // TODO 토스트 띄우는 동작 제거 후 예약 정보 화면으로 이동하도록 변경
        reservationHistoryAdapter = ReservationHistoryAdapter { makeToast(requireContext(), "ripple test") }
        binding.rvReservationHistory.adapter = reservationHistoryAdapter
        val dividerItemDecoration = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.rvReservationHistory.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
