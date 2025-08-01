package com.joshayoung.notemark.note.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.notemark.note.data.database.dao.NoteDao
import com.joshayoung.notemark.note.data.database.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}