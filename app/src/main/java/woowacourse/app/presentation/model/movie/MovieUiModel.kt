package woowacourse.app.presentation.model.movie

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize
import woowacourse.app.presentation.model.HomeData
import woowacourse.app.presentation.ui.main.home.adapter.HomeViewType
import java.time.LocalDate

@Parcelize
data class MovieUiModel(
    override val id: Long,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val screeningDates: List<LocalDate>,
    val runningTime: Int,
    val description: String,
    @DrawableRes val thumbnail: Int,
    @DrawableRes val poster: Int,
) : HomeData(), Parcelable {
    override val homeViewType: HomeViewType = HomeViewType.CONTENT
}
