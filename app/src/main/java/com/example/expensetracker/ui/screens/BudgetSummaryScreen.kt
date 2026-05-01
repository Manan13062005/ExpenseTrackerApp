package com.example.expensetracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.viewmodel.ExpenseViewModel

@Composable
fun BudgetSummaryScreen(viewModel: ExpenseViewModel) {

    val totalSpent by viewModel.totalMonthlySpent.collectAsState()
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val budget by viewModel.budget.collectAsState()
    val remaining by viewModel.remaining.collectAsState()

    var input by rememberSaveable { mutableStateOf("") }
    var showKeypad by remember { mutableStateOf(false) }

    val percent = if (budget == 0.0) 0.0 else (totalSpent * 100 / budget)

    fun formatAmount(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            "₹${amount.toInt()}"
        } else {
            "₹%.2f".format(amount)
        }
    }

    LaunchedEffect(budget) {
        if (input.isEmpty() && budget > 0) {
            input = budget.toInt().toString()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Budget & Summary",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        item {
            Card(
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Set Monthly Budget",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showKeypad = true },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("₹", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (input.isEmpty()) "Enter amount" else input,
                                style = MaterialTheme.typography.titleLarge,
                                color = if (input.isEmpty())
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val value = input.toDoubleOrNull()
                            if (value != null) {
                                viewModel.setBudget(value)
                                input = ""
                                showKeypad = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(if (budget == 0.0) "Set Budget" else "Update Budget")
                    }

                    if (showKeypad) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            CalculatorKeypad(
                                onNumberClick = { value ->
                                    if (value == "." && input.contains(".")) return@CalculatorKeypad
                                    input += value
                                },
                                onDelete = {
                                    if (input.isNotEmpty()) {
                                        input = input.dropLast(1)
                                    }
                                },
                                onDone = {
                                    showKeypad = false
                                }
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Summary",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Total Spent: ${formatAmount(totalSpent)}")
                    Text("Budget: ${formatAmount(budget)}")
                    Text("Remaining: ${formatAmount(remaining)}")

                    Spacer(modifier = Modifier.height(12.dp))

                    when {
                        percent > 100 -> Text("❌ Budget exceeded", color = Color.Red)
                        percent > 80 -> Text("⚠ Near budget", color = Color(0xFFFFA000))
                        percent > 0 -> Text("✅ Within budget", color = Color(0xFF2E7D32))
                    }
                }
            }
        }

        item {
            Card(
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Category-wise Spending",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (categoryTotals.isEmpty()) {
                        Text("No expenses added yet")
                    } else {
                        categoryTotals.entries.forEach { (category, amount) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(category)
                                Text(formatAmount(amount))
                            }
                        }
                    }
                }
            }
        }
    }
}