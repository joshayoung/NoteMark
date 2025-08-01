package com.joshayoung.notemark.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.notemark.data.database.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}