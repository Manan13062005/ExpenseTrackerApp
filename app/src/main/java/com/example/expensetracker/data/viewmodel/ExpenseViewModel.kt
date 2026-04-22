package com.example.expensetracker.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.database.ExpenseDatabase
import com.example.expensetracker.data.local.entity.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    val totalSpent: StateFlow<Double> = allExpenses
        .map { list -> list.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    val categoryTotals: StateFlow<Map<String, Double>> = allExpenses
        .map { list ->
            list.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())

    private val _budget = MutableStateFlow(0.0)
    val budget: StateFlow<Double> = _budget

    fun setBudget(amount: Double) {
        _budget.value = amount
    }

    val remaining: StateFlow<Double> = combine(totalSpent, budget) { spent, budget ->
        budget - spent
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)
}