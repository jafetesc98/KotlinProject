package org.example.project.domain

import org.example.project.model.Expense
import org.example.project.model.ExpensesCategory

interface ExpenseRepository{
    suspend fun getAllExpenses(): List<Expense>

    suspend fun addExpense(expense: Expense)

    suspend fun editExpense(expense: Expense)

    suspend fun deleteExpense(id: Long)

    fun getCategories(): List<ExpensesCategory>
}