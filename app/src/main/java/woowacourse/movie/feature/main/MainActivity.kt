package woowacourse.movie.feature.main

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import woowacourse.movie.R
import woowacourse.movie.feature.common.OnDataUpdate
import woowacourse.movie.feature.common.Toaster
import woowacourse.movie.feature.movieList.MovieListFragment
import woowacourse.movie.feature.reservationList.ReservationListFragment
import woowacourse.movie.feature.setting.SettingFragment
import woowacourse.movie.util.requestPermissions

class MainActivity : AppCompatActivity() {
    private val containerView: FragmentContainerView by lazy { findViewById(R.id.container) }
    private val bottomNavigation: BottomNavigationView by lazy { findViewById(R.id.bottom_navigation_view) }

    private lateinit var movieListFragment: MovieListFragment
    private lateinit var reservationListFragment: ReservationListFragment
    private lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieListFragment = getFragment(MOVIE_LIST_TAG)
        reservationListFragment = getFragment(RESERVATION_LIST_TAG)
        settingFragment = getFragment(SETTING_TAG)

        if (savedInstanceState == null) {
            initFragments()
        }

        initListener()
        requestPermissions(PERMISSIONS, requestPermissionLauncher::launch)
    }

    private inline fun <reified T : Fragment> getFragment(tag: String): T {
        return supportFragmentManager.findFragmentByTag(tag) as? T
            ?: T::class.java.getDeclaredConstructor().newInstance()
    }

    private fun initFragments() {
        supportFragmentManager.commit {
            add(containerView.id, movieListFragment, MOVIE_LIST_TAG)
            add(containerView.id, reservationListFragment, RESERVATION_LIST_TAG)
            add(containerView.id, settingFragment, SETTING_TAG)
            hide(reservationListFragment)
            hide(settingFragment)
        }
        bottomNavigation.selectedItemId = R.id.movie_list_item
    }

    private fun initListener() {
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.reservation_list_item -> {
                    changeShowFragment<ReservationListFragment>()
                }
                R.id.movie_list_item -> {
                    changeShowFragment<MovieListFragment>()
                }
                R.id.setting_item -> {
                    changeShowFragment<SettingFragment>()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private inline fun <reified T : Fragment> changeShowFragment() {
        supportFragmentManager.commit {
            supportFragmentManager.fragments.forEach {
                if (it is T) {
                    show(it)
                    it.updateData()
                } else {
                    hide(it)
                }
            }
        }
    }

    private fun Fragment.updateData() {
        if (this is OnDataUpdate) onUpdateData()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toaster.showToast(this, getString(R.string.alarm_notification_approve))
        } else {
            Toaster.showToast(this, getString(R.string.alarm_notification_reject))
        }
    }

    companion object {
        internal const val KEY_MOVIE = "key_movie"
        internal const val KEY_ADV = "key_adb"

        private const val RESERVATION_LIST_TAG = "reservation_list_tag"
        private const val MOVIE_LIST_TAG = "movie_list_tag"
        private const val SETTING_TAG = "setting_tag"

        val PERMISSIONS = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
        )
    }
}
