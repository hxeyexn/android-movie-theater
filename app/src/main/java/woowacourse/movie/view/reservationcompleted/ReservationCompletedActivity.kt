package woowacourse.movie.view.reservationcompleted

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import woowacourse.movie.databinding.ActivityReservationCompletedBinding
import woowacourse.movie.util.getParcelableCompat
import woowacourse.movie.view.model.ReservationUiModel
import woowacourse.movie.view.moviemain.MovieMainActivity

class ReservationCompletedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationCompletedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reservation =
            intent.getParcelableCompat<ReservationUiModel>(RESERVATION)
        reservation?.let { initViewData(it) }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    toMovieMainActivity()
                }
            },
        )
    }

    private fun initViewData(reservation: ReservationUiModel) {
        binding.reservation = reservation
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                toMovieMainActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toMovieMainActivity() {
        val intent = Intent(this, MovieMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    companion object {
        private const val RESERVATION = "RESERVATION"
        const val REQUEST_CODE = 101

        fun newIntent(context: Context, reservation: ReservationUiModel): Intent {
            val intent = Intent(context, ReservationCompletedActivity::class.java)
            intent.putExtra(RESERVATION, reservation)
            return intent
        }
    }
}
