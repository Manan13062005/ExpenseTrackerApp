package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("""
    SELECT * FROM expenses 
    WHERE strftime('%m', date) = strftime('%m', 'now') 
    AND strftime('%Y', date) = strftime('%Y', 'now')
""")
    fun getCurrentMonthExpenses(): Flow<List<Expense>>

}
