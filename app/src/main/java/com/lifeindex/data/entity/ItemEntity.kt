package com.lifeindex.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imagePath: String,
    val title: String,
    val category: String,
    val tagsJson: String, // JSON array of tags
    val createTime: Long = System.currentTimeMillis(),
    val analysis: String = "" // AI analysis result
)
