package com.maochun.testudpbroadcast

import android.content.Context
import java.net.*

class UDPBroadcaster(var mContext: Context) {

    private var mPort = 0
    private var mSocket: DatagramSocket? = null

    fun open(port: Int): Boolean {
        mPort = port
        try {
            mSocket = DatagramSocket(port)
            mSocket?.broadcast = true
            mSocket?.reuseAddress = true
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun close(): Boolean {
        if (mSocket != null && mSocket?.isClosed?.not() as Boolean) {
            mSocket?.close()
        }
        return true
    }

    fun sendPacket(buffer: ByteArray): Boolean {
        try {
            val packet = DatagramPacket(buffer, buffer.size)
            packet.address = InetAddress.getByName("255.255.255.255")
            packet.port = mPort
            mSocket?.send(packet)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun recvPacket(buffer: ByteArray): Boolean {
        val packet = DatagramPacket(buffer, buffer.size)
        try {
            mSocket?.receive(packet)
            println("recv packet ${packet.address} ${packet.data}")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}