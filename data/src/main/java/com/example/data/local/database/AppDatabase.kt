package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.QuestionDao
import com.example.data.local.entity.QuestionEntity

// Zwiększono wersję do 2 z powodu zmiany struktury (Unique Index)
@Database(entities = [QuestionEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}
