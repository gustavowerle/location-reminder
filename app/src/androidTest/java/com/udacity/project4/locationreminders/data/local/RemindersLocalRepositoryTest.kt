package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = RemindersLocalRepository(
            database.reminderDao(),
            Dispatchers.Main
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    // I needed to use runBlocking instead of runBlockingTest
    // because the second one doesn't worked here
    @Test
    fun saveRemindersAndGetReminders() = runBlocking {
        val reminder1 = ReminderDTO(
            "Test",
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble(),
            UUID.randomUUID().toString()
        )
        val reminder2 = ReminderDTO(
            "Test 1",
            "",
            "Test 1",
            "1234".toDouble(),
            "1234".toDouble(),
            UUID.randomUUID().toString()
        )

        repository.saveReminder(reminder1)
        repository.saveReminder(reminder2)

        val result = repository.getReminders() as Result.Success

        Assert.assertEquals(reminder1, result.data[0])
        Assert.assertEquals(reminder2, result.data[1])
    }

    @Test
    fun saveRemindersAndGetReminderById() = runBlocking {
        val id = UUID.randomUUID().toString()
        val reminder = ReminderDTO(
            "Test",
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble(),
            id
        )

        repository.saveReminder(reminder)

        val result = repository.getReminder(id) as Result.Success

        Assert.assertEquals(reminder, result.data)
    }

    @Test
    fun deleteAllRemindersAndTryToGetReminders() = runBlocking {
        val reminder1 = ReminderDTO(
            "Test",
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble(),
            UUID.randomUUID().toString()
        )
        val reminder2 = ReminderDTO(
            "Test 1",
            "",
            "Test 1",
            "1234".toDouble(),
            "1234".toDouble(),
            UUID.randomUUID().toString()
        )

        repository.saveReminder(reminder1)
        repository.saveReminder(reminder2)
        repository.deleteAllReminders()

        val obtained = repository.getReminders() as Result.Success

        Assert.assertTrue(obtained.data.isEmpty())
    }
}
