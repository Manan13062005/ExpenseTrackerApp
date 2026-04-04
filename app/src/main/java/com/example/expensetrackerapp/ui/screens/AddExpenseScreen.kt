package com.example.expensetrackerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onAddExpense: (Expense) -> Unit,
    onBack: () -> Unit
) {

    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Food 🍔") }

    val categories = listOf(
        "Food 🍔",
        "Travel 🚗",
        "Shopping 🛍️",
        "Miscellaneous 📦"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Add Expense", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("What did you spend on?") },
            modifier = Modifier.fillMaxWidth()
        )

        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox (
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selectedCategory = it
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val color = when (selectedCategory) {
                    "Food 🍔" -> androidx.compose.ui.graphics.Color(0xFFFF7043)
                    "Travel 🚗" -> androidx.compose.ui.graphics.Color(0xFF42A5F5)
                    "Shopping 🛍️" -> androidx.compose.ui.graphics.Color(0xFF66BB6A)
                    else -> androidx.compose.ui.graphics.Color(0xFFAB47BC)
                }

                onAddExpense(
                    Expense(
                        category = selectedCategory,
                        title = title,
                        amount = "₹$amount",
                        color = color
                    )
                )
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Expense")
        }
    }
}

