package com.example.tugas_pertemuan_12_room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 2, exportSchema = false)

abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null
        fun getDatabase(context: Context): NoteRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NoteRoomDatabase::class.java, "note_database"
                    )
                        .addMigrations(MIGRATION_2_1)
                        .build()
                }
            }
            return INSTANCE
        }

        private val MIGRATION_2_1: Migration = object : Migration(2, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }
}