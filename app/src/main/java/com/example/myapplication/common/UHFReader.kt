package com.example.myapplication.common

import android.util.Log
import com.rscja.deviceapi.RFIDWithUHFUART
import com.rscja.deviceapi.entity.UHFTAGInfo

class UHFReader {
    private val uhfReader: RFIDWithUHFUART? = RFIDWithUHFUART.getInstance()
    private var isScanning = false

    // Connect to the reader and return true if successful
    fun init(): Boolean = uhfReader?.init() ?: false

    fun scan(onGetResult: (isScanning: Boolean, data: ArrayList<String>) -> Unit) {
        val epcCodes = arrayListOf<String>()
        if (uhfReader?.startInventoryTag() == true) {
            isScanning = true
            Thread {
                while (!Thread.currentThread().isInterrupted && isScanning) {
                    val tagInfo = uhfReader.readTagFromBuffer()

                    log(tagInfo)

                    if (tagInfo != null && !epcCodes.contains(tagInfo.epc)) {
                        epcCodes.add(tagInfo.epc)
                    }
                }
            }.start()
        } else {
            isScanning = false
            Thread.currentThread().interrupt()
            stopScan()
        }
        onGetResult(isScanning, epcCodes)
    }

    fun readOnce(onGetResult: (data: String) -> Unit) {
        val tagInfo: UHFTAGInfo? = uhfReader?.inventorySingleTag()

        log(tagInfo)

        if (tagInfo != null) {
            onGetResult(tagInfo.epc ?: "")
        }
    }

    private fun log(tagInfo: UHFTAGInfo?) {
        if (tagInfo != null) {
            Log.d(
                "TAG",
                "epc: ${tagInfo.epc}, epcBytes: ${tagInfo.epcBytes}, tid: ${tagInfo.tid}, rssi: ${tagInfo.rssi}, count: ${tagInfo.count}, pc: ${tagInfo.pc}, user: ${tagInfo.user}, reserved: ${tagInfo.reserved}, ant: ${tagInfo.ant}, remain: ${tagInfo.remain}, index: ${tagInfo.index}"
            )
        } else {
            Log.d("TAG", "tag: null")
        }
    }

    fun stopScan() {
        uhfReader?.stopInventory()
    }

    fun destroy() {
        uhfReader?.free()
    }
}