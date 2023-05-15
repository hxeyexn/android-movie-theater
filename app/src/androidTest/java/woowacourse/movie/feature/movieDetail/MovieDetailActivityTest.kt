package woowacourse.movie.feature.movieDetail

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.domain.repository.dataSource.movieDataSources
import com.example.domain.repository.dataSource.theaterDataSources
import com.example.domain.repository.dataSource.theaterMovieScreeningTimesDataSources
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import woowacourse.movie.R
import woowacourse.movie.feature.detail.MovieDetailActivity
import woowacourse.movie.feature.seatSelect.SeatSelectActivity
import woowacourse.movie.feature.util.checkMatches
import woowacourse.movie.model.SelectTheaterAndMovieState
import woowacourse.movie.model.mapper.asPresentation

@RunWith(AndroidJUnit4::class)
@LargeTest
class MovieDetailActivityTest {

    private val theater = theaterDataSources[0]

    // 더 퍼스트 슬램덩크 1
    private val movie = movieDataSources[0]

    private val intent = MovieDetailActivity.getIntent(
        ApplicationProvider.getApplicationContext(),
        SelectTheaterAndMovieState(
            theater.asPresentation(), movie.asPresentation(),
            theaterMovieScreeningTimesDataSources[0].screeningInfos.find { it.movie == movie }?.screeningDateTimes
                ?: listOf()
        )
    )

    @get:Rule
    val activityRule = ActivityScenarioRule<MovieDetailActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun `영화_제목이_보여진다`() {
        onView(withId(R.id.detail_title))
            .checkMatches(withText(movie.title))
    }

    @Test
    fun `영화_상영일이_보여진다`() {
        onView(withId(R.id.detail_date))
            .checkMatches(withText("상영일: 2023.4.29 ~ 2023.5.20"))
    }

    @Test
    fun `영화_줄거리가_보여진다`() {
        onView(withId(R.id.description))
            .checkMatches(withText(movie.description))
    }

    @Test
    fun `plus_버튼을_누르면_티켓_개수가_1증가한다`() {
        onView(withId(R.id.plus))
            .checkMatches(isDisplayed())
            .perform(click())

        onView(withId(R.id.count))
            .checkMatches(withText("2"))
    }

    @Test
    fun `minus_버튼을_누르면_티켓_개수가_1감소한다`() {
        onView(withId(R.id.plus))
            .checkMatches(isDisplayed())
            .perform(click())

        onView(withId(R.id.minus))
            .checkMatches(isDisplayed())
            .perform(click())

        onView(withId(R.id.count))
            .checkMatches(withText("1"))
    }

    @Test
    fun `좌석_선택_화면으로_넘어간다`() {
        onView(withId(R.id.plus))
            .checkMatches(isDisplayed())
            .perform(click())

        onView(withId(R.id.reservation_confirm))
            .checkMatches(isDisplayed())
            .perform(click())

        Intents.intended(IntentMatchers.hasComponent(SeatSelectActivity::class.java.name))
    }
}
