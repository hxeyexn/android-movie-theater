package woowacourse.movie.domain.dataSource

import woowacourse.movie.domain.Movie
import woowacourse.movie.domain.MovieMock

class MovieDataSource : DataSource<Movie> {
    override val value: List<Movie>
        get() = data

    override fun add(t: Movie) {
        data.add(t)
    }

    companion object {
        private val data: MutableList<Movie> =
            List(2500) { MovieMock.createMovies() }.flatten().toMutableList()
    }
}
