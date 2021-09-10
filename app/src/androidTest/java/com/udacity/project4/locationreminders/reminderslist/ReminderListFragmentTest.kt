package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.FakeDataSourceForAndroidTest
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.util.getTestModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

    private lateinit var repository: FakeDataSourceForAndroidTest
    private lateinit var database: RemindersDatabase
    private lateinit var navController: NavController

    @Before
    fun setup() {
        stopKoin()

        navController = mock(NavController::class.java)

        val application: Application = ApplicationProvider.getApplicationContext()

        database = Room.inMemoryDatabaseBuilder(
            application,
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = FakeDataSourceForAndroidTest()

        startKoin {
            androidContext(application)
            modules(getTestModules(database, repository))
        }

    }

    @After
    fun tearDown() {
        stopKoin()
        database.close()
    }

    @Test
    fun reminderListFragmentTestWithNoData() {
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.noDataTextView)).check(matches(withText("No Data")))
    }

    @Test
    fun reminderListFragmentTestWithData() = runBlockingTest {
        val reminder = ReminderDTO(
            "Title Test",
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble()
        )
        repository.saveReminder(reminder)

        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withText("Title Test")).check(matches(isDisplayed()))
    }

    @Test
    fun reminderListFragmentTestNavigationToSaveReminderFragment() {
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.addReminderFAB)).perform(ViewActions.click())

        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }
}
