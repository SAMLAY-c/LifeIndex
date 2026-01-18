package com.lifeindex.data.repository

import com.lifeindex.data.dao.ItemDao
import com.lifeindex.data.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {

    fun getAllItems(): Flow<List<ItemEntity>> = itemDao.getAllItems()

    suspend fun getItemById(itemId: Long): ItemEntity? = itemDao.getItemById(itemId)

    fun searchItems(query: String): Flow<List<ItemEntity>> = itemDao.searchItems(query)

    fun getItemsByCategory(category: String): Flow<List<ItemEntity>> =
        itemDao.getItemsByCategory(category)

    suspend fun insertItem(item: ItemEntity): Long = itemDao.insertItem(item)

    suspend fun updateItem(item: ItemEntity) = itemDao.updateItem(item)

    suspend fun deleteItem(item: ItemEntity) = itemDao.deleteItem(item)
}
