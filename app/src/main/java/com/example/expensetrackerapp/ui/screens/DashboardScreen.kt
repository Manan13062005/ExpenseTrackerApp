package com.example.expensetrackerapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme


data class Expense(
    val category: String,
    val amount: String,
    val color : Color
)

@Composable
fun DashboardScreen() {

    val expenses = listOf(
        Expense("Food 🍔", "₹200", Color(0xFFFF7043)),
        Expense("Travel 🚗", "₹50", Color(0xFF42A5F5)),
        Expense("Shopping 🛍️", "₹500", Color(0xFF66BB6A)),
        Expense("Miscellaneous ", "₹150", Color(0xFFAB47BC))
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(expenses) { expense ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
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

                            // 🔵 LEFT SIDE (color dot + category)
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
                                    text = expense.category,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

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
@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    ExpenseTrackerAppTheme {
        DashboardScreen()
    }
}
