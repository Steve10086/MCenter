package com.astune.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigration {
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE device ADD COLUMN last_delay TEXT")
        }
    }
    val MIGRATION_2_3: Migration = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE weblink RENAME COLUMN address TO port")

            database.execSQL("ALTER TABLE sshlink RENAME COLUMN address TO port")
            database.execSQL("ALTER TABLE sshlink ADD COLUMN username TEXT NOT NULL")
            database.execSQL("ALTER TABLE sshlink ADD COLUMN password TEXT NOT NULL")
        }
    }
    val MIGRATION_3_4: Migration = object :Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE device ADD COLUMN enable_delay INT NOT NULL DEFAULT 1")
        }
    }
}