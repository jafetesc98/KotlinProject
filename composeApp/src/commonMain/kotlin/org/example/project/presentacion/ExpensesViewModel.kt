package org.example.project.presentacion

import moe.tlaster.precompose.viewmodel.ViewModel
import org.example.project.domain.ExpenseRepository
import org.example.project.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.model.ExpensesCategory


data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0
)

class ExpensesViewModel(private val repo : ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState = _uiState.asStateFlow()
    private val allExpenses = repo.getAllExpenses()

    init {
        getAllexpenses()
    }

    private fun getAllexpenses(){
        viewModelScope.launch {
            _uiState.update{ state ->
                state.copy(expenses = allExpenses, total = allExpenses.sumOf { it.amount })
            }
        }
    }

    fun addExpense(expense: Expense){
        viewModelScope.launch {
            repo.addExpense(expense)
            updateState()

        }
    }

    fun deleteExpense(expense: Expense){
        viewModelScope.launch {
            repo.deleteExpense(expense)
            updateState()

        }
    }

    fun editExpense(expense: Expense){
        viewModelScope.launch {
            repo.editExpense(expense)
            updateState()
        }

    }

    private fun updateState(){
        _uiState.update { state ->
            state.copy(expenses = allExpenses, total = allExpenses.sumOf { it.amount })
        }
    }
    fun getExpensesWithId(id: Long): Expense{
        return allExpenses.first { it.id == id }
    }

    fun getCategories():List<ExpensesCategory>{
        return repo.getCategories()

    }

}