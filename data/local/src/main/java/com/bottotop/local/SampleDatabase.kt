package com.bottotop.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class SampleDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
}