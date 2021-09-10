package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SaveReminderViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @Before
    fun setup() {
        fakeDataSource = FakeDataSource()

        viewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun validateAndSaveReminderTestWithInvalidTitle() {
        val reminder = ReminderDataItem(
            null,
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble(),
            UUID.randomUUID().toString()
        )

        viewModel.validateAndSaveReminder(reminder)

        assertEquals(R.string.err_enter_title, viewModel.showSnackBarInt.value)
    }

    @Test
    fun validateAndSaveReminderTestWithInvalidLocation() {
        val reminder = ReminderDataItem(
            "Test",
            "",
            null,
            "123".toDouble(),
            "123".toDouble(),
            UUID.randomUUID().toString()
        )

        viewModel.validateAndSaveReminder(reminder)

        assertEquals(R.string.err_select_location, viewModel.showSnackBarInt.value)
    }

    @Test
    fun validateAndSaveReminderTestWithValidData() = runBlocking {
        val id = UUID.randomUUID().toString()
        val reminder = ReminderDataItem(
            "Test",
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble(),
            id
        )
        mainCoroutineRule.pauseDispatcher()
        viewModel.validateAndSaveReminder(reminder)

        assertTrue(viewModel.showLoading.value!!)
        mainCoroutineRule.resumeDispatcher()

        val result = fakeDataSource.getReminder(id) as Result.Success

        assertEquals(reminder.id, result.data.id)

        assertFalse(viewModel.showLoading.value!!)
    }
}
