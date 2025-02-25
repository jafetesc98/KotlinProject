package org.example.project.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
//import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.viewmodel.viewModel
import org.example.project.data.ExpenseManager
import org.example.project.data.ExpenseRepoImp
import org.example.project.getColorsTheme
import org.example.project.presentacion.ExpensesViewModel
import org.example.project.ui.ExpenseDetailScreen
import org.example.project.ui.ExpensesScreen
import org.koin.core.parameter.parametersOf

@Composable
fun Navigator(navigator: Navigator) {

    val colors = getColorsTheme()

    /*val viewModel = viewModel(modelClass = ExpensesViewModel::class) {
        ExpensesViewModel(ExpenseRepoImp(ExpenseManager))
    }*/
    val viewModel = koinViewModel(ExpensesViewModel::class){ parametersOf() }

    NavHost(
        modifier = Modifier.background(colors.backgroundColor),
        navigator = navigator,
        initialRoute = "/home"
    ) {
        scene(route = "/home") {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ExpensesScreen(uiState = uiState, onExpenseClick = {expense ->
                navigator.navigate("/addExpenses/${expense.id}")
            }, onDeleteExpense = { expenseToDelete ->
                viewModel.deleteExpense(expenseToDelete.id)
            })
        }
        scene(route = "/addExpenses/{id}?") { backStackEntry ->
            val idFromPath = backStackEntry.path<Long>("id")
            val expensetoEditOrAdd = idFromPath?.let { id -> viewModel.getExpensesWithId(id) }

            //crear ExpensesDetailScreen
            ExpenseDetailScreen(expenseToEdit = expensetoEditOrAdd, categoryList = viewModel.getCategories()) { expense ->
                if (expensetoEditOrAdd == null) {
                    viewModel.addExpense(expense)
                } else {
                    viewModel.editExpense(expense)
                }
                navigator.popBackStack()
            }


        }
    }
}