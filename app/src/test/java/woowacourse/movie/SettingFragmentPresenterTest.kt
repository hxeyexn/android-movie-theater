package woowacourse.movie

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.movie.fragment.setting.contract.SettingContract
import woowacourse.movie.fragment.setting.contract.presenter.SettingPresenter
import woowacourse.movie.util.preference.DataPreference

internal class SettingFragmentPresenterTest {

    private lateinit var presenter: SettingPresenter
    private lateinit var view: SettingContract.View
    private val dataPreference = mockk<DataPreference>()

    @Before
    fun setUp() {
        view = mockk()
        presenter = SettingPresenter(view, dataPreference)
    }

    @Test
    fun `저장된 데이터가 잘 불러와지는지 확인`() {
        val slot = slot<Boolean>()
        every { dataPreference.loadData() } returns true
        every { view.setSwitchState(capture(slot)) } just Runs

        presenter.onLoadData()

        assertEquals(slot.captured, true)
        verify { view.setSwitchState(slot.captured) }
    }
}
