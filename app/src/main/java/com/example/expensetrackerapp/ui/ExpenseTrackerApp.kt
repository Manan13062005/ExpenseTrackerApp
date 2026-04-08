package com.example.expensetrackerapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetrackerapp.ui.screens.AddExpenseScreen
import com.example.expensetrackerapp.ui.screens.DashboardScreen
import com.example.expensetrackerapp.ui.screens.Expense
import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

@Composable
fun ExpenseTrackerApp(){
    ExpenseTrackerAppTheme {

        val navController = rememberNavController()

        var expenses by remember {
            mutableStateOf(
                emptyList<Expense>()
            )
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            NavHost(navController, startDestination = "dashboard") {

                composable("dashboard") {
                    DashboardScreen(
                        expenses = expenses,
                        onAddClick = {
                            navController.navigate("add")
                        },
                        onDeleteExpense = { expense ->
                            expenses = expenses - expense
                        }
                    )
                }

                composable("add") {
                    AddExpenseScreen(
                        onAddExpense = { newExpense ->
                            expenses = expenses + newExpense
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}