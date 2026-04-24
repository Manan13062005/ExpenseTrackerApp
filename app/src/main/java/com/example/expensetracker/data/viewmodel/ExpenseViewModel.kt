package com.example.expensetracker.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.database.ExpenseDatabase
import com.example.expensetracker.data.local.entity.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()
    private val repository = ExpenseRepository(dao)

    val allExpenses = repository.allExpenses

    fun insert(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }

    fun addExpense(expense: Expense) = insert(expense)

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }

    val totalMonthlySpent: StateFlow<Double> = allExpenses
        .map { list ->
            val calendar = Calendar.getInstance()
            list.filter { expense ->
                val expenseCal = Calendar.getInstance()
                expenseCal.timeInMillis = expense.date

                calendar.get(Calendar.MONTH) == expenseCal.get(Calendar.MONTH) &&
                        calendar.get(Calendar.YEAR) == expenseCal.get(Calendar.YEAR)
            }.sumOf { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    val categoryTotals: StateFlow<Map<String, Double>> = allExpenses
        .map { list ->
            list.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())

    private val _budget = MutableStateFlow(0.0)
    val budget: StateFlow<Double> = _budget

    init {
        val prefs = getApplication<Application>()
            .getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)

        _budget.value = prefs.getFloat("budget", 0f).toDouble()
    }

    fun setBudget(amount: Double) {
        _budget.value = amount

        val prefs = getApplication<Application>()
            .getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)

        prefs.edit().putFloat("budget", amount.toFloat()).apply()
    }

    val remaining: StateFlow<Double> = combine(totalMonthlySpent, budget) { spent, budget ->
        budget - spent
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    fun getPreviousMonthsCategoryTotals(): Flow<Map<String, Double>> {
        return allExpenses.map { list ->
            val calendar = java.util.Calendar.getInstance()
            val currentMonth = calendar.get(java.util.Calendar.MONTH)
            val currentYear = calendar.get(java.util.Calendar.YEAR)

            list.filter { expense ->
                val expenseCal = java.util.Calendar.getInstance()
                expenseCal.timeInMillis = expense.date

                expenseCal.get(java.util.Calendar.YEAR) < currentYear ||
                        (expenseCal.get(java.util.Calendar.YEAR) == currentYear &&
                                expenseCal.get(java.util.Calendar.MONTH) < currentMonth)
            }
                .groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
        }
    }

}