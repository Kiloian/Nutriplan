package com.nutriplan.app.data.repository

import com.nutriplan.app.data.dao.ShoppingItemDao
import com.nutriplan.app.data.entities.ShoppingItem
import kotlinx.coroutines.flow.Flow

class ShoppingListRepository(private val shoppingItemDao: ShoppingItemDao) {
    val allItems: Flow<List<ShoppingItem>> = shoppingItemDao.getAllItems()

    suspend fun insertItem(item: ShoppingItem) = shoppingItemDao.insertItem(item)
    suspend fun updateItem(item: ShoppingItem) = shoppingItemDao.updateItem(item)
    suspend fun deleteItem(item: ShoppingItem) = shoppingItemDao.deleteItem(item)
    suspend fun updateItemCheckedStatus(id: Long, isChecked: Boolean) =
        shoppingItemDao.updateItemCheckedStatus(id, isChecked)
}