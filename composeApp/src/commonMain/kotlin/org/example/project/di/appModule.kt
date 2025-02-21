package org.example.project.di

import com.expenseApp.db.AppDatabase
import org.example.project.data.ExpenseManager
import org.example.project.data.ExpenseRepoImp
import org.example.project.domain.ExpenseRepository
import org.example.project.presentacion.ExpensesViewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

fun appModule(appDatabase: AppDatabase) = module {
    single { ExpenseManager }.withOptions { createdAtStart() }
    single<ExpenseRepository> { ExpenseRepoImp(get(),appDatabase) }
    factory { ExpensesViewModel(get()) }
}