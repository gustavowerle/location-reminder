package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

    private var remindersData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()
    private var shouldShowError = false

    fun setError(value: Boolean) {
        shouldShowError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldShowError) {
            return Result.Error(ERROR_MESSAGE)
        }
        return Result.Success(remindersData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remindersData[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldShowError) {
            return Result.Error(ERROR_MESSAGE)
        }
        return if (remindersData[id] != null) {
            Result.Success(remindersData[id]!!)
        } else {
            Result.Error("Error")
        }
    }

    override suspend fun deleteAllReminders() {
        remindersData.clear()
    }

    companion object {
        const val ERROR_MESSAGE = "ERROR MESSAGE"
    }
}
