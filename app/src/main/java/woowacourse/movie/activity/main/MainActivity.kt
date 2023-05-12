package woowacourse.movie.activity.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import woowacourse.movie.R
import woowacourse.movie.activity.main.contract.MainContract
import woowacourse.movie.activity.main.contract.presenter.MainPresenter
import woowacourse.movie.databinding.ActivityMainBinding
import woowacourse.movie.fragment.history.HistoryFragment
import woowacourse.movie.fragment.home.HomeFragment
import woowacourse.movie.fragment.setting.SettingFragment
import woowacourse.movie.util.permission.PermissionManager
import woowacourse.movie.util.preference.SettingPreference

class MainActivity : AppCompatActivity(), MainContract.View {

    override val presenter: MainContract.Presenter by lazy {
        MainPresenter(
            this,
            SettingPreference(this),
        )
    }
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean -> presenter.saveSettingData(isGranted) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        checkPermisson()
        showFragment<HomeFragment>(HomeFragment.TAG)
        onClickBottomNavItem()
    }

    private fun onClickBottomNavItem() {
        binding.navigationBar.selectedItemId = R.id.home
        binding.navigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.history -> showFragment<HistoryFragment>(HistoryFragment.TAG)
                R.id.home -> showFragment<HomeFragment>(HomeFragment.TAG)
                R.id.setting -> showFragment<SettingFragment>(SettingFragment.TAG)
            }
            true
        }
    }

    private fun checkPermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionManager.requestNotificationPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
                requestPermissionLauncher,
            )
        }
    }

    private inline fun <reified T : Fragment> showFragment(tag: String) {
        manageFragment<T>(tag)
    }

    private inline fun <reified T : Fragment> manageFragment(tag: String) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            supportFragmentManager.fragments.forEach(::hide)
            val fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)
            if (fragment == null) {
                add<T>(R.id.fragment_container_view, tag)
            } else {
                show(fragment)
            }
        }
    }
}
