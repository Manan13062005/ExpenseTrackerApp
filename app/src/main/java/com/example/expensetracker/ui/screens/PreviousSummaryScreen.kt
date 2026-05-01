package com.example.expensetracker.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.viewmodel.ExpenseViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensetracker.ui.theme.ExpenseTrackerAppTheme
import androidx.compose.foundation.shape.RoundedCornerShape

fun formatAmount(amount: Double): String {
    return if (amount % 1.0 == 0.0) {
        "₹${amount.toInt()}"
    } else {
        "₹%.2f".format(amount)
    }
}

@Composable
fun PreviousSummaryScreen(viewModel: ExpenseViewModel) {

    val previousTotals by viewModel.getPreviousMonthsCategoryTotals()
        .collectAsState(initial = emptyMap())

    val mostSpent = previousTotals.maxByOrNull { it.value }

    PreviousSummaryContent(
        categoryTotals = previousTotals,
        mostSpent = mostSpent
    )
}

@Composable
fun PreviousSummaryContent(
    categoryTotals: Map<String, Double>,
    mostSpent: Map.Entry<String, Double>?
) {

    val total = categoryTotals.values.sum()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 40.dp)
    ) {

        Text(
            text = "Previous Summary",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (categoryTotals.isEmpty()) {
            Text("No previous data available")
        } else {

            Text(
                text = "Total: ${formatAmount(total)}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

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

                    categoryTotals.forEach { (category, amount) ->
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

            Spacer(modifier = Modifier.height(16.dp))

            mostSpent?.let {
                MostSpentCategoryCard(
                    category = it.key,
                    amount = it.value
                )
            }
        }
    }
}

@Composable
fun MostSpentCategoryCard(category: String, amount: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Most Spent Category",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = formatAmount(amount),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviousSummaryPreview() {

    val fakeData = mapOf(
        "Food 🍔" to 3000.0,
        "Travel 🚗" to 1500.0,
        "Shopping 🛍️" to 2000.0
    )

    val mostSpent = fakeData.maxByOrNull { it.value }

    ExpenseTrackerAppTheme {
        PreviousSummaryContent(fakeData, mostSpent)
    }
}