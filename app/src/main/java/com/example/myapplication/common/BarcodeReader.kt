package com.example.myapplication.common

import android.content.Context
import android.util.Log
import com.example.myapplication.ResultState
import com.rscja.barcode.BarcodeDecoder
import com.rscja.barcode.BarcodeFactory

class BarcodeReader(private val context: Context) {
    private val barcodeDecoder: BarcodeDecoder? = BarcodeFactory.getInstance().barcodeDecoder

    fun open() {
        barcodeDecoder?.open(context)
    }

    fun onDecodeCallback(onGetResult: (ResultState<String>) -> Unit) {
        barcodeDecoder?.setDecodeCallback { barcodeEntity ->
            Log.e("TAG", "onDecodeCallback: ${barcodeEntity}")
            if (barcodeEntity.resultCode == BarcodeDecoder.DECODE_SUCCESS) {
                Log.e("TAG", "onDecodeCallback: ${barcodeEntity.barcodeData}")
                onGetResult(ResultState.Success(barcodeEntity.barcodeData))
            } else {
                Log.e("TAG", "Decode failed")
                onGetResult(ResultState.Error(code = 0, message = "Decode failed"))
            }
        }
    }

    fun startScan() {
        barcodeDecoder?.startScan()
    }

    fun stopScan() {
        barcodeDecoder?.stopScan()
    }

    fun close() {
        barcodeDecoder?.close()
    }
}