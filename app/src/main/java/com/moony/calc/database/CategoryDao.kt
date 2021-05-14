package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.Category

@Dao
interface CategoryDao {
    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("select*from category_table where isIncome = :isIncome")
    fun getAllCategory(isIncome: Boolean): LiveData<List<Category>>

    @Query("select * from category_table where idCategory=:id")
    fun getCategory(id: String): LiveData<Category>

    @Query("select * from category_table where isIncome=:isIncome and resId = :resId")
    fun getSavingCategory(isIncome: Boolean, resId: Int): LiveData<Category>
}