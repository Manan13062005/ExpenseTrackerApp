package com.example.expensetracker.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.database.ExpenseDatabase
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.launch
import com.example.expensetracker.data.local.entity.Expense

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()
    private val repository = ExpenseRepository(dao)

    val allExpenses = repository.allExpenses

    fun insert(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }
}