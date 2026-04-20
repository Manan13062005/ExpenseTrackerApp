package com.example.expensetracker.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.local.entity.Expense
import com.example.expensetracker.ui.theme.ExpenseTrackerAppTheme
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onAddExpense: (Expense) -> Unit,
    onBack: () -> Unit
) {

    var amount by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Food 🍔") }
    var showKeypad by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

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

        // 🔼 ALL CONTENT (including button)
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("What did you spend on?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) showKeypad = false
                    }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {},
                readOnly = true,
                label = { Text("Amount") },
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            focusManager.clearFocus()
                            showKeypad = true
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                    showKeypad = false
                    focusManager.clearFocus()
                }
            ) {

                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

            // ✅ SAVE BUTTON (correct position)
            if (!showKeypad) {
                Button(
                    onClick = {
                        val amountDouble = amount.toDoubleOrNull()

                        if (title.isNotBlank() && amountDouble != null && amountDouble > 0) {
                            onAddExpense(
                                Expense(
                                    id = 0,
                                    title = title,
                                    amount = amountDouble,
                                    category = selectedCategory,
                                    date = System.currentTimeMillis()
                                )
                            )
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding() // 🔥 prevents bottom overlap
                ) {
                    Text("Save Expense")
                }
            }
        }

        // 🔽 PUSH KEYPAD TO BOTTOM
        if (showKeypad) {
            CalculatorKeypad(
                onNumberClick = { amount += it },
                onDelete = {
                    if (amount.isNotEmpty()) {
                        amount = amount.dropLast(1)
                    }
                },
                onDone = { showKeypad = false }
            )
        }
    }
}

@Composable
fun CalculatorKeypad(
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    onDone: () -> Unit
) {
    val buttons = listOf(
        listOf("7","8","9"),
        listOf("4","5","6"),
        listOf("1","2","3"),
        listOf(".","0","⌫")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(8.dp)
    ) {

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { symbol ->
                    Button(
                        onClick = {
                            if (symbol == "⌫") onDelete()
                            else onNumberClick(symbol)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(symbol)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddExpenseScreenPreview() {
    ExpenseTrackerAppTheme {
        AddExpenseScreen(
            onAddExpense = {},
            onBack = {}
        )
    }
}