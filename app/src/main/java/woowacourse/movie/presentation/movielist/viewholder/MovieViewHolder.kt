package woowacourse.movie.presentation.movielist.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import woowacourse.movie.R
import woowacourse.movie.presentation.movielist.MovieItem
import woowacourse.movie.presentation.util.formatDotDate

class MovieViewHolder(private val view: View, clickBook: (Long) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.textItemTitle)
    private val screeningDate: TextView = view.findViewById(R.id.textBookingScreeningDate)
    private val runningTime: TextView = view.findViewById(R.id.textBookingRunningTime)
    private val itemBook: Button = view.findViewById(R.id.buttonItemBook)
    private val thumbnail: ImageView = view.findViewById(R.id.imageItemThumbnail)

    init {
        itemBook.setOnClickListener { clickBook((adapterPosition + ADDITIONAL_POSITION).toLong()) }
    }

    fun bind(item: MovieItem.Movie) {
        val movie = item.movie
        title.text = movie.title
        runningTime.text = view.context.getString(R.string.running_time)
            .format(movie.runningTime)
        thumbnail.setImageResource(movie.thumbnail)
        screeningDate.apply {
            text = context.getString(R.string.screening_date)
                .format(
                    movie.screeningStartDate.formatDotDate(),
                    movie.screeningEndDate.formatDotDate()
                )
        }
    }

    companion object {
        private const val ADDITIONAL_POSITION = 1
    }
}
