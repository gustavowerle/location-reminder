package com.udacity.project4.locationreminders.reminderslist

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import java.util.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RemindersListViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @Before
    fun setup() {
        fakeDataSource = FakeDataSource()

        viewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun loadRemindersTestNoDataResult() {
        viewModel.loadReminders()

        assertTrue(viewModel.showNoData.getOrAwaitValue())
    }

    @Test
    fun loadRemindersTestLoadingFlow() {
        mainCoroutineRule.pauseDispatcher()
        viewModel.loadReminders()

        assertTrue(viewModel.showLoading.getOrAwaitValue())

        mainCoroutineRule.resumeDispatcher()
        assertFalse(viewModel.showLoading.getOrAwaitValue())
    }

    @Test
    fun loadRemindersTest() = runBlocking {
        val reminder = ReminderDTO(
            "Test",
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble(),
            UUID.randomUUID().toString()
        )
        fakeDataSource.saveReminder(reminder)

        viewModel.loadReminders()

        assertTrue(viewModel.remindersList.value!!.isNotEmpty())
    }

    @Test
    fun loadRemindersTestShowErrorMessage() {
        fakeDataSource.setError(true)

        viewModel.loadReminders()

        assertEquals(FakeDataSource.ERROR_MESSAGE, viewModel.showSnackBar.value)
    }
}
