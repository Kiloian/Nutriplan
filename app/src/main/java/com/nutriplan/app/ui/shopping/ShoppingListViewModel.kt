package com.nutriplan.app.ui.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriplan.app.data.dao.ShoppingItemDao
import com.nutriplan.app.data.entities.ShoppingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingItemDao: ShoppingItemDao
) : ViewModel() {
    val allItems: Flow<List<ShoppingItem>> = shoppingItemDao.getAllItems()

    fun addItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingItemDao.insertItem(item)
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingItemDao.deleteItem(item)
        }
    }

    fun updateItemCheckedStatus(id: Long, isChecked: Boolean) {
        viewModelScope.launch {
            shoppingItemDao.updateItemCheckedStatus(id, isChecked)
        }
    }
}