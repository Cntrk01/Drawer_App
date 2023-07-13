package com.paint.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paint.database.dataclass.PaintData
import java.util.concurrent.Flow

@androidx.room.Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraw(draw : List<PaintData>)

    @Query("SELECT * FROM tableDraw")
    //Cursor hatası vermişti ArrayList kullanmıştım.List ile çözüldü...
    fun getDraw() : LiveData<List<PaintData>>

    @Query("DELETE FROM tableDraw")
    suspend fun deleteDraw()



}