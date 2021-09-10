package com.udacity.project4.util

import com.udacity.project4.locationreminders.data.local.FakeDataSourceForAndroidTest
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getTestModules(
    database: RemindersDatabase,
    fakeDataSource: FakeDataSourceForAndroidTest
) = module {
    viewModel {
        RemindersListViewModel(
            get(),
            get() as FakeDataSourceForAndroidTest
        )
    }
    single {
        SaveReminderViewModel(
            get(),
            get() as FakeDataSourceForAndroidTest
        )
    }
    single { fakeDataSource }
    single { database.reminderDao() }
}