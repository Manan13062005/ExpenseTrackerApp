package com.example.expensetrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme


data class Expense(
    val category: String,
    val title: String,
    val amount: String,
    val color: Color
)

@Composable
fun DashboardScreen(
    expenses: List<Expense>,
    onAddClick: () -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
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

            // 🔥 Total Expense Card (static for now)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Total Expense",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "₹1200",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Daily", "Weekly", "Monthly").forEach {
                    FilterChip(
                        selected = it == "Daily",
                        onClick = { },
                        label = { Text(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Recent Expenses",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 CATEGORY-WISE LIST
            LazyColumn {

                val categories = listOf(
                    "Food 🍔",
                    "Travel 🚗",
                    "Shopping 🛍️",
                    "Miscellaneous 📦"
                )

                categories.forEach { category ->

                    val filteredList = expenses.filter {
                        it.category == category
                    }

                    if (filteredList.isNotEmpty()) {

                        // 🔹 Category Title
                        item {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // 🔹 Expenses under that category
                        items(filteredList) { expense ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    // 🔵 Left side
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(expense.color, shape = CircleShape)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = expense.title,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }

                                    // 🔴 Amount
                                    Text(
                                        text = expense.amount,
                                        color = expense.color,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    ExpenseTrackerAppTheme {
        DashboardScreen(
            expenses = listOf(
                Expense("Food 🍔", "burger","₹200", Color(0xFFFF7043)),
                Expense("Travel 🚗", "metro","₹500", Color(0xFF42A5F5))
            ),
            onAddClick = {}
        )
    }
}
