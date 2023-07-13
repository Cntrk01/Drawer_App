package com.paint.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.paint.database.Dao
import com.paint.database.dataclass.PaintData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DrawerRepository @Inject constructor(private val dao: Dao) {
     suspend fun insertDraw(draw : List<PaintData>) = dao.insertDraw(draw)

    fun getDraw() : LiveData<List<PaintData>>{
        return dao.getDraw()
    }
    suspend fun deleteDraw()=dao.deleteDraw()



}