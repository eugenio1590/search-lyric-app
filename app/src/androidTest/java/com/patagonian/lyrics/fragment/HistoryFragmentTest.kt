package com.patagonian.lyrics.fragment

import android.content.Context
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.patagonian.lyrics.R
import com.patagonian.lyrics.fragment.history.HistoryFragment
import com.patagonian.lyrics.util.setup
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val navController = TestNavHostController(context)

    private val historyScenario: FragmentScenario<HistoryFragment>
        get() = launchFragmentInContainer(themeResId = R.style.Theme_Lyrics)

    @Test
    fun testNavigationToSearchLyricScreen() {
        val searchLyricMenuItem = ActionMenuItem(context, 0, R.id.action_search_lyric, 0, 0, null)
        historyScenario.onFragment { fragment ->
            fragment.setup(navController)
            fragment.onOptionsItemSelected(searchLyricMenuItem)
            assertThat(navController.currentDestination?.id).isEqualTo(R.id.searchLyricFragment)
        }
    }
}