package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.common.BarcodeReader
import com.example.myapplication.common.UHFReader
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.usecases.InitUHFReaderUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                val viewModel = MainViewModel(InitUHFReaderUseCase())

                // case barcode reader init val
                val bardcodeReader = remember { mutableStateOf(BarcodeReader(this)) }

                // A surface container using the 'background' color from the theme
                val requester = remember { FocusRequester() }

                LaunchedEffect(Unit) {
                    // case barcode init here
//                    bardcodeReader.value.open()
//                    bardcodeReader.value.onDecodeCallback { result ->
//                        Log.d("TAG", "onCreate: ${result.data}")
//                    }
                    requester.requestFocus()
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .onKeyEvent { event ->
                            val action = event.nativeKeyEvent.action
                            val keyCode = event.nativeKeyEvent.keyCode

                            if (action == KeyEvent.ACTION_DOWN && (keyCode == 139 || keyCode == 280 || keyCode == 293)) {
                                // Case Hold button down
                                viewModel.uhfReader.data?.readOnce(onGetResult = { result ->
                                    Log.d("TAG", "onGetResult: $result")
                                    if (!viewModel.epcs.contains(result)) {
                                        viewModel.epcs.add(result)
                                    }
                                })

                                // Case Hold once then scan buffer, when hold again, it will stop scan
//                            viewModel.uhfReader.data?.scan(onGetResult = { isScanning, result ->
//                                    Log.d("TAG", "onGetResult: $result")
//                                    viewModel.onChangeScanning(isScanning)
//
//                                    if (isScanning) {
//                                        viewModel.onAddAllRFID(result)
//                                    }
//                                })

                                // Case barcode / QR Code
                                bardcodeReader.value.startScan()
                            }

                            if (action == KeyEvent.ACTION_UP && (keyCode == 139 || keyCode == 280 || keyCode == 293)) {
                                // update state scanning to false
                                // Case Hold button up
//                                bardcodeReader.value.stopScan()
                            }
                            true
                        }
                        .focusRequester(requester)
                        .focusable(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(
                        text = when (viewModel.uhfReader) {
                            is ResultState.Loading -> "Loading"
                            is ResultState.Success -> "Connected"
                            is ResultState.Idle -> "Ready To Scan"
                            is ResultState.Error -> "Error"
                        }
                    )
                }
            }
        }
    }
}