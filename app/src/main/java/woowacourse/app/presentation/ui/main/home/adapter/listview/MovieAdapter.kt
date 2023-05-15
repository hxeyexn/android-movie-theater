package woowacourse.app.presentation.ui.main.home.adapter.listview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import woowacourse.app.presentation.model.HomeData
import woowacourse.app.presentation.ui.main.home.adapter.HomeViewType
import woowacourse.movie.R

class MovieAdapter(
    context: Context,
    private val clickBook: (Long) -> Unit,
    private val clickAd: (Intent) -> Unit,
) : BaseAdapter() {
    private val movies = mutableListOf<HomeData>()
    private val layoutInflater = LayoutInflater.from(context)
    private val viewHolders = mutableMapOf<Int, MainViewHolder>()

    override fun getCount(): Int = movies.size

    override fun getItem(position: Int): HomeData = movies[position]

    override fun getItemId(position: Int): Long = movies[position].id

    override fun getItemViewType(position: Int): Int = movies[position].homeViewType.ordinal

    override fun getViewTypeCount(): Int = 2

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewType = HomeViewType.getMainViewType(getItemViewType(position))
        val layoutId: Int = when (viewType) {
            HomeViewType.CONTENT -> R.layout.movie_list_item
            HomeViewType.ADVERTISEMENT -> R.layout.advertisement_list_item
        }

        val view: View = convertView ?: layoutInflater.inflate(layoutId, parent, false)
        onBindViewHolder(position, view.hashCode(), view)
        return view
    }

    private fun onBindViewHolder(position: Int, key: Int, view: View) {
        val viewType = HomeViewType.getMainViewType(getItemViewType(position))
        val item = getItem(position)
        if (viewHolders[key] == null) {
            viewHolders[key] = viewType.makeViewHolder(view)
            setViewHolderClick(viewHolders[key] ?: return)
        }
        viewHolders[key]?.onBind(item)
    }

    private fun setViewHolderClick(viewHolder: MainViewHolder) {
        when (viewHolder) {
            is MovieViewHolder -> viewHolder.clickBookButton(clickBook)
            is AdvertisementViewHolder -> viewHolder.clickAdvertisement(clickAd)
        }
    }

    fun initMovies(items: List<HomeData>) {
        movies.clear()
        movies.addAll(items)
        notifyDataSetChanged()
    }
}
