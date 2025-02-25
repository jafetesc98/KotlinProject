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


/*data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0
)*/
sealed class ExpensesUiState{
    object Loading : ExpensesUiState()
    data class Success(val expenses: List<Expense>, val total: Double) : ExpensesUiState()
    data class Error(val message: String) : ExpensesUiState()
}

class ExpensesViewModel(private val repo : ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesUiState>(ExpensesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getExpenseList()
    }

    private fun getExpenseList(){
        viewModelScope.launch {
            try {
                val expenses = repo.getAllExpenses()
                _uiState.value = ExpensesUiState.Success(expenses, expenses.sumOf { it.amount })
            }catch (e: Exception){
                _uiState.value = ExpensesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun updateExpenseList(){
        /*viewModelScope.launch {
            allExpenses = repo.getAllExpenses().toMutableList()
            updateState()
        }*/
        try {
            val expenses = repo.getAllExpenses()
            _uiState.value = ExpensesUiState.Success(expenses, expenses.sumOf { it.amount })
        }catch (e:Exception){
            _uiState.value = ExpensesUiState.Error(e.message ?: "Unknown error")
        }
    }

    fun addExpense(expense: Expense){
        /*viewModelScope.launch {
            repo.addExpense(expense)
            updateState()

        }*/
        viewModelScope.launch {
            try {
                repo.addExpense(expense)
                updateExpenseList()
            }catch (e: Exception){
                _uiState.value = ExpensesUiState.Error(e.message ?: "Unknown error")

            }
        }
    }

    fun editExpense(expense: Expense){
        /*viewModelScope.launch {
            repo.editExpense(expense)
            updateState()
        }*/
        viewModelScope.launch {
            try {
                repo.editExpense(expense)
                updateExpenseList()
            }catch (e: Exception){
                _uiState.value = ExpensesUiState.Error(e.message ?: "Unknown error")

            }
        }

    }

    fun deleteExpense(expense: Expense){
        /*viewModelScope.launch {
            repo.deleteExpense(expense)
            updateState()

        }*/viewModelScope.launch {
            try {
                repo.deleteExpense(expense)
                updateExpenseList()
            }catch (e: Exception){
                _uiState.value = ExpensesUiState.Error(e.message ?: "Unknown error")

            }
        }

    }

    /*private fun getAllexpenses(){
        viewModelScope.launch {
            _uiState.update{ state ->
                state.copy(expenses = allExpenses, total = allExpenses.sumOf { it.amount })
            }
        }
    }*/


    fun getExpensesWithId(id: Long): Expense?{
        //return allExpenses.first { it.id == id }
        return (_uiState.value as? ExpensesUiState.Success)?.expenses?.firstOrNull() { it.id == id }
    }

    fun getCategories():List<ExpensesCategory>{
        return repo.getCategories()

    }

}