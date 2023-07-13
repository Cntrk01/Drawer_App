package com.paint.database

import android.graphics.Paint
import android.graphics.Path
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.paint.PaintPath

class Converters {
    @TypeConverter
    fun fromPaintPath(value: PaintPath): String {
        val pathJson = Gson().toJson(value.path)
        val paintJson = Gson().toJson(value.paint)
        return "$pathJson|$paintJson"
    }

    @TypeConverter
    fun toPaintPath(value: String): PaintPath {
        val jsonParts = value.split("|")
        val path = Gson().fromJson(jsonParts[0], Path::class.java)
        val paint = Gson().fromJson(jsonParts[1], Paint::class.java)
        return PaintPath(path, paint)
    }
}