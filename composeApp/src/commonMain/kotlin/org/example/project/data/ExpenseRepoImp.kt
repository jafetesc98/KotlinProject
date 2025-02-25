package org.example.project.data

import com.expenseApp.db.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.domain.ExpenseRepository
import org.example.project.model.Expense
import org.example.project.model.ExpensesCategory
import org.example.project.model.NetworkExpense


private const val BASE_URL = "http://172.16.3.127:8080"

class ExpenseRepoImp(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient
) : ExpenseRepository {

    private val queries = appDatabase.expensesDbQueries


    override suspend fun getAllExpenses(): List<Expense> {
        return if (queries.selectAll().executeAsList().isEmpty()) {
            val networkResponse = httpClient.get("$BASE_URL/expenses").body<List<NetworkExpense>>()
            val expenses = networkResponse.map { networkExpense ->
                Expense(
                    id = networkExpense.id,
                    amount = networkExpense.amount,
                    category = ExpensesCategory.valueOf(networkExpense.category),
                    description = networkExpense.description
                )
            }
            expenses.forEach {
                queries.insert(it.amount, it.category.name,it.description)
            }
            expenses
        } else {
            queries.selectAll().executeAsList().map {
                Expense(
                    id = it.id,
                    amount = it.amount,
                    category = ExpensesCategory.valueOf(it.category),
                    description = it.description
                )
            }
        }


    }

    override suspend fun addExpense(expense: Expense) {
        queries.transaction {
            queries.insert(
                amount = expense.amount,
                category = expense.category.name,
                description = expense.description
            )
        }
        //expenseManager.addNewExpense(expense)
        httpClient.get("$BASE_URL/expenses") {
            contentType(ContentType.Application.Json)
            setBody(
                NetworkExpense(
                    amount = expense.amount,
                    category = expense.category.name,
                    description = expense.description
                )
            )
        }
    }

    override suspend fun editExpense(expense: Expense) {
        queries.transaction {
            queries.update(
                id = expense.id,
                amount = expense.amount,
                category = expense.category.name,
                description = expense.description
            )
        }
        //expenseManager.editExpense(expense)
        httpClient.put("$BASE_URL/expenses/${expense.id}") {
            contentType(ContentType.Application.Json)
            setBody(
                NetworkExpense(
                    id = expense.id,
                    amount = expense.amount,
                    category = expense.category.name,
                    description = expense.description
                )
            )
        }
    }

    override suspend fun deleteExpense(expense: Expense): List<Expense> {
        TODO("")
        //expenseManager.deleteExpense(expense)
    }

    override fun getCategories(): List<ExpensesCategory> {
        return queries.categories().executeAsList().map {
            ExpensesCategory.valueOf(it)
        }
        //return expenseManager.getCategories()
    }

}