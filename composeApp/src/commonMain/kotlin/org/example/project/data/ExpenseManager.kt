package org.example.project.data

import org.example.project.model.Expense
import org.example.project.model.ExpensesCategory

object ExpenseManager{
    private var currentId = 1L

    val fakeExpenseList = mutableListOf(
        Expense(
            id= currentId++,
            amount = 100.0,
            category = ExpensesCategory.GROCERIES,
            description = "Weekly buy"
        ),
        Expense(
            id= currentId++,
            amount = 70.0,
            category = ExpensesCategory.SNACKS,
            description = "Kiss"
        ),
        Expense(
            id= currentId++,
            amount = 560.0,
            category = ExpensesCategory.CAR,
            description = "VW"
        ),
        Expense(
            id= currentId++,
            amount = 541.0,
            category = ExpensesCategory.PARTY,
            description = "Weekend party"
        ),
        Expense(
            id= currentId++,
            amount = 5453.0,
            category = ExpensesCategory.HOUSE,
            description = "Cleaning"
        ),
        Expense(
            id= currentId++,
            amount = 70.0,
            category = ExpensesCategory.OTHER,
            description = "Services"
        )
    )

    fun addNewExpense(expense: Expense){
        fakeExpenseList.add(expense.copy(id = currentId++))
    }

    fun editExpense(expense: Expense){
        var index = fakeExpenseList.indexOfFirst { it.id == expense.id }

        if(index != -1){
            fakeExpenseList[index] = fakeExpenseList[index].copy(
                amount = expense.amount,
                category = expense.category,
                description = expense.description
            )
        }
    }
    fun deleteExpense(expense: Expense){
        val index = fakeExpenseList.indexOfFirst { it.id == expense.id }

        fakeExpenseList.removeAt(index)
    }
    fun getCategories():List<ExpensesCategory>{
        return listOf(
            ExpensesCategory.GROCERIES,
            ExpensesCategory.PARTY,
            ExpensesCategory.SNACKS,
            ExpensesCategory.COFFEE,
            ExpensesCategory.CAR,
            ExpensesCategory.HOUSE,
            ExpensesCategory.OTHER
        )
    }
}