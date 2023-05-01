package woowacourse.movie.presentation.complete

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import woowacourse.movie.R
import woowacourse.movie.data.MovieData
import woowacourse.movie.domain.model.tools.Movie
import woowacourse.movie.presentation.main.MainActivity
import woowacourse.movie.presentation.model.TicketModel
import woowacourse.movie.presentation.util.formatDotDateTimeColon

class CompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)

        val ticket: TicketModel = getTicket()
        initView(ticket)
    }

    private fun getTicket(): TicketModel {
        val ticketModel: TicketModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TICKET, TicketModel::class.java)
                ?: throw IllegalArgumentException()
        } else {
            intent.getParcelableExtra(TICKET) ?: throw IllegalArgumentException()
        }
        return ticketModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startMainWithClearBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(ticket: TicketModel) {
        val movie = MovieData.findMovieById(ticket.movieId)
        setMovieTitle(movie)
        setMovieScreeningDate(ticket)
        setMovieTicketCount(ticket)
        setMoviePaymentAmount(ticket)
    }

    private fun setMovieTitle(movie: Movie) {
        findViewById<TextView>(R.id.textCompletedTitle).text = movie.title
    }

    private fun setMovieScreeningDate(ticket: TicketModel) {
        findViewById<TextView>(R.id.textCompletedScreeningDate).text =
            ticket.bookedDateTime.formatDotDateTimeColon()
    }

    private fun setMovieTicketCount(ticket: TicketModel) {
        findViewById<TextView>(R.id.textCompletedTicketCount).text =
            getString(R.string.normal_ticket_count_seat).format(
                ticket.count,
                ticket.formatSeatsCombine(),
            )
    }

    private fun setMoviePaymentAmount(ticket: TicketModel) {
        findViewById<TextView>(R.id.textCompletedPaymentAmount).text =
            getString(R.string.payment_on_site_amount).format(ticket.paymentMoney)
    }

    private fun startMainWithClearBackStack() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TICKET = "TICKET"

        fun getIntent(context: Context, ticketModel: TicketModel): Intent {
            return Intent(context, CompleteActivity::class.java).apply {
                putExtra(TICKET, ticketModel)
            }
        }

        private fun TicketModel.formatSeatsCombine(): String {
            val stringBuilder = StringBuilder()
            this.seats.forEach { stringBuilder.append("$it ") }
            return stringBuilder.toString()
        }
    }
}
