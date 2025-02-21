package org.example.project.data

//import com.expenseApp.db.AppDatabase
import com.expenseApp.db.AppDatabase
import org.example.project.domain.ExpenseRepository
import org.example.project.model.Expense
import org.example.project.model.ExpensesCategory

class ExpenseRepoImp(
    private val expenseManager: ExpenseManager,
    private val appDatabase: AppDatabase
): ExpenseRepository {

    private val queries = appDatabase.expensesDbQueries


    override fun getAllExpenses(): List<Expense> {
        return queries.selectAll().executeAsList().map {
            Expense(
                id=it.id,
                amount=it.amount,
                category=ExpensesCategory.valueOf(it.category),
                description=it.description
            )
        }
        //return expenseManager.fakeExpenseList
    }

    override fun addExpense(expense: Expense) {
        queries.transaction {
            queries.insert(
                amount = expense.amount,
                category = expense.category.name,
                description = expense.description
            )
        }
        //expenseManager.addNewExpense(expense)
    }

    override fun editExpense(expense: Expense) {
        queries.transaction {
            queries.update(
                id=expense.id,
                amount = expense.amount,
                category = expense.category.name,
                description = expense.description
            )
        }
        //expenseManager.editExpense(expense)
    }

    override fun deleteExpense(expense: Expense) {
        expenseManager.deleteExpense(expense)
    }

    override fun getCategories(): List<ExpensesCategory> {
        return queries.categories().executeAsList().map {
            ExpensesCategory.valueOf(it)
        }
        //return expenseManager.getCategories()
    }

}