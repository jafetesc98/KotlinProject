package org.example.project.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.getColorsTheme
import org.example.project.model.Expense
import org.example.project.presentacion.ExpensesUiState
import org.example.project.utils.SwipeToDeleteContainer


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    uiState: ExpensesUiState, onExpenseClick: (Expense) -> Unit,
    onDeleteExpense: (expense: Expense) -> Unit
) {

    val colors = getColorsTheme()
    when (uiState) {
        is ExpensesUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ExpensesUiState.Success -> {
            if(uiState.expenses.isEmpty()){
                Box (
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No expenses found, please add your first expense with the + symbol down below",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1,
                        color= colors.textColor
                    )
                }
            }else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stickyHeader {
                        Column(
                            modifier = Modifier.background(colors.backgroundColor)
                        ) {
                            //composables
                            ExpensesTotalHearde(uiState.total)
                            AllExpensesHeader()
                        }
                    }
                    items(items = uiState.expenses, key = { it.id }) { expense ->
                        //composable
                        SwipeToDeleteContainer(
                            item = expense,
                            onDelete = onDeleteExpense
                        ) {
                            ExpensesItem(expense = expense, onExpenseClick = onExpenseClick)
                        }
                    }
                }
            }

        }

        is ExpensesUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${uiState.message}", style = MaterialTheme.typography.body1)
            }
        }
    }


}

@Composable
fun ExpensesTotalHearde(total: Double) {
    Card(shape = RoundedCornerShape(30), backgroundColor = Color.Black, elevation = 5.dp) {
        Box(
            modifier = Modifier.fillMaxWidth().height(130.dp).padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "$$total",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            )
            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = "USD",
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AllExpensesHeader() {
    val colors = getColorsTheme()

    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "All Expenses",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textColor

        )
        Button(
            shape = RoundedCornerShape(50),
            onClick = { },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            enabled = false
        ) {
            Text("View All")
        }
    }
}

@Composable
fun ExpensesItem(expense: Expense, onExpenseClick: (Expense) -> Unit) {
    val colors = getColorsTheme()

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp).clickable {
            onExpenseClick(expense)
        },
        shape = RoundedCornerShape(30),
        backgroundColor = colors.colorExpenseItem
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(35),
                color = colors.purple
            ) {
                Image(
                    modifier = Modifier.padding(10.dp),
                    imageVector = expense.icon,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Imagen Icono"
                )
            }
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    text = expense.category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.textColor
                )
                Text(
                    text = expense.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.textColor
                )
            }
            Text(
                text = "$${expense.amount}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textColor
            )

        }
    }
}