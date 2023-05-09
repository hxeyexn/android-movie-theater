package woowacourse.movie.view.moviemain.movielist

import woowacourse.movie.domain.Ad
import woowacourse.movie.domain.Movie
import woowacourse.movie.domain.repository.MovieRepository
import woowacourse.movie.domain.repository.TheaterRepository
import woowacourse.movie.view.mapper.toMovieTheater
import woowacourse.movie.view.mapper.toUiModel
import woowacourse.movie.view.model.MovieListModel

class MovieListPresenter(
    private val view: MovieListContract.View,
    private val movieRepository: MovieRepository,
    private val theaterRepository: TheaterRepository
) : MovieListContract.Presenter {

    override fun loadMovieList() {
        val movies = movieRepository.findAll()
        val dataList = generateMovieListData(movies)
        view.showMovieList(dataList)
    }

    private fun generateMovieListData(movies: List<Movie>): List<MovieListModel> {
        val ad = Ad(
            1,
            "https://woowacourse.github.io/"
        ).toUiModel()

        return mixMovieAdData(movies, ad, AD_POST_INTERVAL)
    }

    private fun mixMovieAdData(
        movies: List<Movie>,
        ad: MovieListModel.MovieAdModel?,
        adPostInterval: Int
    ): List<MovieListModel> {
        return movies.flatMapIndexed { index, movie ->
            if (index % adPostInterval == adPostInterval - 1 && ad != null) {
                listOf(movie.toUiModel(), ad)
            } else {
                listOf(movie.toUiModel())
            }
        }
    }

    override fun decideNextAction(item: MovieListModel) {
        when (item) {
            is MovieListModel.MovieUiModel -> {
                loadTheaterList(item)
            }
            is MovieListModel.MovieAdModel -> {
                view.toAdScreen(item)
            }
        }
    }

    override fun loadTheaterList(movie: MovieListModel.MovieUiModel) {
        val theaters = theaterRepository.findTheaterByMovieId(movie.id)
        view.showTheaterList(theaters.map { it.toMovieTheater(movie.id) }, movie)
    }

    companion object {
        private const val AD_POST_INTERVAL = 3
    }
}
