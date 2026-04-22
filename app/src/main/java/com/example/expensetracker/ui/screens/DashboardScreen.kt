package com.example.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.expensetracker.ui.theme.ExpenseTrackerAppTheme
import com.example.expensetracker.data.local.entity.Expense
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardScreen(
    expenses: List<Expense>,
    onAddClick: () -> Unit,
    onDeleteExpense: (Expense) -> Unit,
    navController: NavController
) {

    var selectedFilter by remember { mutableStateOf("Daily") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }

    val calendar = Calendar.getInstance()

    val filteredExpenses = expenses.filter { expense ->

        val expenseCal = Calendar.getInstance()
        expenseCal.timeInMillis = expense.date

        when (selectedFilter) {
            "Daily" -> {
                calendar.get(Calendar.DAY_OF_YEAR) == expenseCal.get(Calendar.DAY_OF_YEAR) &&
                        calendar.get(Calendar.YEAR) == expenseCal.get(Calendar.YEAR)
            }

            "Weekly" -> {
                calendar.get(Calendar.WEEK_OF_YEAR) == expenseCal.get(Calendar.WEEK_OF_YEAR) &&
                        calendar.get(Calendar.YEAR) == expenseCal.get(Calendar.YEAR)
            }

            "Monthly" -> {
                calendar.get(Calendar.MONTH) == expenseCal.get(Calendar.MONTH) &&
                        calendar.get(Calendar.YEAR) == expenseCal.get(Calendar.YEAR)
            }

            else -> true
        }
    }

    val total = filteredExpenses.sumOf { it.amount }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Expense")
                    Text(
                        text = "₹$total",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navController.navigate("budget_summary") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Budget & Summary")
            }

            Spacer(modifier = Modifier.height(5.dp))


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Daily", "Weekly", "Monthly").forEach { filter ->
                    FilterChip(
                        selected = filter == selectedFilter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Recent Expenses",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {

                val categories = listOf(
                    "Food 🍔",
                    "Travel 🚗",
                    "Shopping 🛍️",
                    "Miscellaneous 📦"
                )

                categories.forEach { category ->

                    val filteredList = filteredExpenses.filter {
                        it.category == category
                    }

                    if (filteredList.isNotEmpty()) {

                        item {

                            val categoryTotal = filteredList.sumOf { it.amount }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "₹$categoryTotal",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        items(filteredList) { expense ->

                            val color = when (expense.category) {
                                "Food 🍔" -> Color(0xFFFF7043)
                                "Travel 🚗" -> Color(0xFF42A5F5)
                                "Shopping 🛍️" -> Color(0xFF66BB6A)
                                else -> Color(0xFFAB47BC)
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Row(verticalAlignment = Alignment.CenterVertically) {

                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(color, CircleShape)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(expense.title)
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {

                                        Text(
                                            text = "₹${expense.amount}",
                                            color = color,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        IconButton(onClick = {
                                            selectedExpense = expense
                                            showDialog = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (filteredExpenses.isEmpty()) {
                    item {
                        Text(
                            text = "No expenses found",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            if (showDialog && selectedExpense != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Delete Expense") },
                    text = {
                        Text("Are you sure you want to delete ${selectedExpense?.title}?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDeleteExpense(selectedExpense!!)
                                showDialog = false
                            }
                        ) {
                            Text("Delete", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    ExpenseTrackerAppTheme {

        val navController = rememberNavController()

        DashboardScreen(
            expenses = listOf(
                Expense(
                    id = 1,
                    title = "Burger",
                    amount = 200.0,
                    category = "Food 🍔",
                    date = System.currentTimeMillis()
                ),
                Expense(
                    id = 2,
                    title = "Metro",
                    amount = 500.0,
                    category = "Travel 🚗",
                    date = System.currentTimeMillis()
                )
            ),
            onAddClick = {},
            onDeleteExpense = {},
            navController = navController
        )
    }
}