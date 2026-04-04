package com.example.expensetrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

import androidx.navigation.compose.*

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme
import com.example.expensetrackerapp.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExpenseTrackerAppTheme {

                val navController = rememberNavController()

                var expenses by remember {
                    mutableStateOf(
                        listOf(
                            Expense("Food 🍔", "burger","₹200", Color(0xFFFF7043))
                        )
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
    }
}