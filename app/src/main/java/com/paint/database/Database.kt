package com.paint.database

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paint.database.dataclass.PaintData

@androidx.room.Database(entities = [PaintData::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun dao():Dao
}