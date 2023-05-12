package woowacourse.movie

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.movie.activity.main.contract.MainContract
import woowacourse.movie.activity.main.contract.presenter.MainPresenter
import woowacourse.movie.util.preference.DataPreference

internal class MainActivityPresenterTest {
    private lateinit var presenter: MainPresenter
    private lateinit var view: MainContract.View
    private lateinit var dataPreference: DataPreference

    @Before
    fun setUp() {
        view = mockk()
        dataPreference = mockk()
        presenter = MainPresenter(view, dataPreference)
    }

    @Test
    fun `데이터가 잘 저장되었는지 확인`() {
        val slot = slot<Boolean>()
        every { dataPreference.saveData(capture(slot)) } answers { println("slot = ${slot.captured}") }

        presenter.saveSettingData(true)

        assertEquals(true, slot.captured)
        verify { dataPreference.saveData(slot.captured) }
    }
}
