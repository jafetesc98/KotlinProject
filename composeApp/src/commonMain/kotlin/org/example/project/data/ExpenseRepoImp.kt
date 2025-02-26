package org.example.project.data

import com.expenseApp.db.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.mapEngineExceptions
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
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
            if(networkResponse.isEmpty()) return emptyList()
            val expenses = networkResponse.map { networkExpense ->
                Expense(
                    id = networkExpense.id,
                    amount = networkExpense.amount,
                    category = ExpensesCategory.valueOf(networkExpense.category),
                    description = networkExpense.description
                )
            }
            expenses.forEach {
                queries.insert(it.id,it.amount, it.category.name,it.description)
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

    /*override suspend fun addExpense(expense: Expense) {
        val networkResponse = httpClient.post("$BASE_URL/expenses") {
            contentType(ContentType.Application.Json)
            setBody(
                NetworkExpense(
                    amount = expense.amount,
                    category = expense.category.name,
                    description = expense.description
                )
            )
        }
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
        /*queries.transaction {
            queries.insert(
                amount = expense.amount,
                category = expense.category.name,
                description = expense.description
            )
        }*/
        //expenseManager.addNewExpense(expense)

    }*/

    override suspend fun addExpense(expense: Expense) {
            // Realiza la solicitud HTTP POST para agregar el gasto y obtener la respuesta como un objeto NetworkExpense
            val networkResponse: NetworkExpense = httpClient.post("$BASE_URL/expenses") {
                contentType(ContentType.Application.Json)
                setBody(
                    NetworkExpense(
                        amount = expense.amount,
                        category = expense.category.name,
                        description = expense.description
                    )
                )
            }.body()

            // Crea una instancia de Expense a partir de la respuesta de la red
            val newExpense = Expense(
                id = networkResponse.id,
                amount = networkResponse.amount,
                category = ExpensesCategory.valueOf(networkResponse.category),
                description = networkResponse.description
            )

            // Inserta el nuevo gasto en la base de datos
            queries.transaction {
                queries.insert(newExpense.id, newExpense.amount, newExpense.category.name, newExpense.description)
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

    override suspend fun deleteExpense(id: Long) {
        httpClient.delete("$BASE_URL/expenses/${id}")
        queries.transaction {
            queries.delete(id=id)
        }
        val expense = queries.selectAll()
        if(expense.executeAsList().isEmpty()) {
            queries.transaction {
                //queries.create_table()
                queries.truncate()
            }
        }else{ println("error no esta vacio")}
    }

    override fun getCategories(): List<ExpensesCategory> {
        return queries.categories().executeAsList().map {
            ExpensesCategory.valueOf(it)
        }
        //return expenseManager.getCategories()
    }

}