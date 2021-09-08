package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveRemindersAndGetReminders() = runBlockingTest {
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
        val dao = database.reminderDao()
        dao.saveReminder(reminder1)
        dao.saveReminder(reminder2)

        val obtained = dao.getReminders()

        assertEquals(reminder1, obtained[0])
        assertEquals(reminder2, obtained[1])
    }

    @Test
    fun saveRemindersAndGetReminderById() = runBlockingTest {
        val id = UUID.randomUUID().toString()
        val reminder = ReminderDTO(
            "Test",
            "",
            "Test",
            "123".toDouble(),
            "123".toDouble(),
            id
        )
        val dao = database.reminderDao()
        dao.saveReminder(reminder)

        val obtained = dao.getReminderById(id)

        assertEquals(reminder, obtained)
    }

    @Test
    fun deleteAllRemindersAndTryToGetReminders() = runBlockingTest {
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
        val dao = database.reminderDao()
        dao.saveReminder(reminder1)
        dao.saveReminder(reminder2)
        dao.deleteAllReminders()

        val obtained = dao.getReminders()

        assertTrue(obtained.isEmpty())
    }

}