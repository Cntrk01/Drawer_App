package com.paint.viewmodel

import androidx.lifecycle.*
import com.paint.database.dataclass.PaintData
import com.paint.repository.DrawerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(private val repository: DrawerRepository) : ViewModel() {


    fun getDrawer(): LiveData<List<PaintData>>{
        return repository.getDraw()
    }

    fun insertDrawer(drawer: List<PaintData>) =viewModelScope.launch {
        repository.insertDraw(drawer)
    }


    fun deleteDraw() =
        viewModelScope.launch {
            repository.deleteDraw()
        }



}