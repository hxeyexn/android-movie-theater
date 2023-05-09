package woowacourse.movie.common.model

import java.io.Serializable

data class MovieViewData(
    val poster: ImageViewData,
    val title: String,
    val date: DateRangeViewData,
    val runningTime: Int,
    val description: String,
    override val viewType: MovieListViewType = MovieListViewType.MOVIE
) : Serializable, MovieListItemViewData {
    companion object {
        const val MOVIE_EXTRA_NAME = "movie"
    }
}
