package com.paint.database.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paint.PaintPath

@Entity(tableName = "tableDraw")
data class PaintData (
        @PrimaryKey(autoGenerate = false)
        var id: Int=0,
        var pathPaint:PaintPath
        )