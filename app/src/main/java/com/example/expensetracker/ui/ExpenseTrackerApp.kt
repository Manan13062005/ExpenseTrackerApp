package com.example.expensetracker.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.*
import com.example.expensetracker.ui.screens.AddExpenseScreen
import com.example.expensetracker.ui.screens.BudgetSummaryScreen
import com.example.expensetracker.ui.screens.DashboardScreen
import com.example.expensetracker.ui.theme.ExpenseTrackerAppTheme
import com.example.expensetracker.data.viewmodel.ExpenseViewModel
import kotlinx.coroutines.delay
import com.example.expensetracker.ui.screens.PreviousSummaryScreen
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpenseTrackerApp() {

    ExpenseTrackerAppTheme {

        val navController = rememberAnimatedNavController()
        val viewModel: ExpenseViewModel = viewModel()
        val expenses by viewModel.allExpenses.collectAsState(initial = emptyList())

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            AnimatedNavHost(
                navController = navController,
                startDestination = "splash",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            ) {

                composable("splash") {
                    SplashScreen(navController)
                }

                composable("dashboard") {
                    DashboardScreen(
                        expenses = expenses,
                        onAddClick = {
                            navController.navigate("add")
                        },
                        onDeleteExpense = { expense ->
                            viewModel.deleteExpense(expense)
                        },
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable("add") {
                    AddExpenseScreen(
                        onAddExpense = { newExpense ->
                            viewModel.addExpense(newExpense)
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("budget_summary") {
                    BudgetSummaryScreen(viewModel)
                }

                composable("previous_summary") {
                    PreviousSummaryScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {

    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(800))
        delay(1500)
        navController.navigate("dashboard") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Expense Tracker",
            modifier = Modifier.scale(scale.value),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}