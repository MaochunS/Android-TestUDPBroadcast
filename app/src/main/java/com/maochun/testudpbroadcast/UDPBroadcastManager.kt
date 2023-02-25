package com.maochun.testudpbroadcast

import android.content.Context


class UDPBroadcastManager(context: Context) {

    val mBroadcaster : UDPBroadcaster = UDPBroadcaster(context)
    var mStopRecv = false

    fun startBroadcast(port:Int, data: ByteArray){
        mBroadcaster.open(56415)
        mStopRecv = false
        mBroadcaster.sendPacket(data)

        /*
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                timer.cancel()
                mBroadcaster.close()
                mStopRecv = true
            }
        }, 10000)
         */

        while (!mStopRecv){
            var buffer = ByteArray(1024)
            mBroadcaster.recvPacket(buffer)

            this.parsingRecvData(buffer)
        }
        println("UDP broadcast done!")
    }

    fun stopBroadcast(){
        mBroadcaster.close()
        mStopRecv = true
    }

    private fun parsingRecvData(data: ByteArray){

        val text = String(data)
        val bin = data.toHex()

        println("recv text: $text")
        println("recv binary: $bin")
    }
}