package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.UHFReader
import com.example.myapplication.usecases.InitUHFReaderUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val initUHFReaderUseCase: InitUHFReaderUseCase) : ViewModel() {
    var uhfReader by mutableStateOf<ResultState<UHFReader>>(ResultState.Loading())
    var epcs = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            initUHFReaderUseCase().collect {
                uhfReader = it
            }
        }
    }

    override fun onCleared() {
        uhfReader.data?.destroy()
        super.onCleared()
    }
}