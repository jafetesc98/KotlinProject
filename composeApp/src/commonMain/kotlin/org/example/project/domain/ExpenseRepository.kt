package org.example.project.domain

import org.example.project.model.Expense
import org.example.project.model.ExpensesCategory

interface ExpenseRepository{
    fun getAllExpenses(): List<Expense>

    fun addExpense(expense: Expense)

    fun editExpense(expense: Expense)

    fun deleteExpense(expense: Expense)

    fun getCategories(): List<ExpensesCategory>
}