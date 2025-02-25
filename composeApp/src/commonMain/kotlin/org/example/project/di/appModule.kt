package org.example.project.di

import com.expenseApp.db.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.example.project.data.ExpenseManager
import org.example.project.data.ExpenseRepoImp
import org.example.project.domain.ExpenseRepository
import org.example.project.presentacion.ExpensesViewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

fun appModule(appDatabase: AppDatabase) = module {
    single <HttpClient>{HttpClient{install(ContentNegotiation){json()}}}
    single<ExpenseRepository> { ExpenseRepoImp(appDatabase,get()) }
    factory { ExpensesViewModel(get()) }
}