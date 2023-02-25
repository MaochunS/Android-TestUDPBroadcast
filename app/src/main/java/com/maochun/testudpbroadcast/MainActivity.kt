package com.maochun.testudpbroadcast

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun ByteArray.toHex(): String = joinToString(separator = " ") { eachByte -> "0x%02x".format(eachByte) }
fun String.toByteArray(): ByteArray =  chunked(2).map { it.toInt(16).toByte() }.toByteArray()

class MainActivity : AppCompatActivity() {

    lateinit var mEditTextPort: EditText
    lateinit var mEditTextMsg: EditText
    lateinit var mEditTextBinMsg: EditText
    lateinit var mButton: Button

    lateinit var mBroadcastMgr: UDPBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditTextPort = findViewById(R.id.editTextPort)
        mEditTextMsg = findViewById(R.id.editTextSendMessage)
        mEditTextBinMsg = findViewById(R.id.editTextBinaryMessage)
        mButton = findViewById(R.id.buttonBroadcast)

        mBroadcastMgr = UDPBroadcastManager(this)
    }

    fun onSendButtonClick(v: View?) {

        val port = mEditTextPort.text.toString().toIntOrNull()
        val text = mEditTextMsg.text.toString()
        val binText = mEditTextBinMsg.text.toString().replace(",", "")

        if (binText.isNotEmpty()) {
            check(binText.length % 2 == 0) {
                Toast.makeText(this, "Invalid binary data", Toast.LENGTH_SHORT).show()
                return
            }
        }

        check(port != null){
            Toast.makeText(this, "Invalid port", Toast.LENGTH_SHORT).show()
            return
        }

        var sendData: ByteArray?=null
        if (text.isNotEmpty()){
            sendData = text.toByteArray()
        }else if (binText.isNotEmpty()){
            sendData = binText.toByteArray()
        }

        check(sendData != null){
            Toast.makeText(this, "Invalid input data", Toast.LENGTH_SHORT).show()
            return
        }

        if (!mBroadcastMgr.mStopRecv){
            Thread{
                mBroadcastMgr.startBroadcast(port!!, sendData!!)
            }.start()
            mButton.text = "Stop Broadcast"
        }else{
            mBroadcastMgr.stopBroadcast()
            mButton.text = "Start Broadcast"
        }
    }
}