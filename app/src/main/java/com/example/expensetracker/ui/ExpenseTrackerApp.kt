package com.example.expensetracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.screens.AddExpenseScreen
import com.example.expensetracker.ui.screens.BudgetSummaryScreen
import com.example.expensetracker.ui.screens.DashboardScreen
import com.example.expensetracker.ui.theme.ExpenseTrackerAppTheme
import com.example.expensetracker.data.viewmodel.ExpenseViewModel

@Composable
fun ExpenseTrackerApp() {

    ExpenseTrackerAppTheme {

        val navController = rememberNavController()

        val viewModel: ExpenseViewModel = viewModel()

        val expenses by viewModel.allExpenses.collectAsState(initial = emptyList())

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            NavHost(
                navController = navController,
                startDestination = "dashboard"
            ) {

                composable("dashboard") {
                    DashboardScreen(
                        expenses = expenses,
                        onAddClick = {
                            navController.navigate("add")
                        },
                        onDeleteExpense = { expense ->
                            viewModel.deleteExpense(expense)   // make sure this exists
                        },
                        navController = navController
                    )
                }

                composable("add") {
                    AddExpenseScreen(
                        onAddExpense = { newExpense ->
                            viewModel.addExpense(newExpense)   // make sure this exists
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("budget_summary") {
                    BudgetSummaryScreen(viewModel)
                }
            }
        }
    }
}