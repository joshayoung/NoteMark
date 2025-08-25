package com.joshayoung.notemark.note.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.notemark.note.data.database.dao.SyncDao
import com.joshayoung.notemark.note.data.database.entity.SyncEntity

@Database(entities = [SyncEntity::class], version = 1)
abstract class SyncDatabase : RoomDatabase() {
    abstract val syncDao: SyncDao

    companion object {
        const val DATABASE_NAME = "notes_sync_db"
    }
}