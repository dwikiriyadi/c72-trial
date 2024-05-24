package com.example.myapplication.usecases

import com.example.myapplication.ResultState
import com.example.myapplication.common.UHFReader
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InitUHFReaderUseCase {
    operator fun invoke(): Flow<ResultState<UHFReader>> = flow {
        emit(ResultState.Loading())

        val reader = UHFReader()

        if (reader.init()) {
            emit(ResultState.Success(reader))

            delay(3000)

            emit(ResultState.Idle(reader))
        } else {
            emit(ResultState.Error(0, "Failed to initialize UHF reader"))
        }
    }
}