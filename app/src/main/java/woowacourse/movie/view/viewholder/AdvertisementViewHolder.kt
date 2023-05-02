package woowacourse.movie.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AdvertisementViewHolder(private val view: View, onClickItem: (Int) -> Unit) :
    RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            onClickItem(adapterPosition)
        }
    }
}
