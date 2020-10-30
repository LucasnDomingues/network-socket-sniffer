package server

import sniffer.Sniffer
import utils.ByteArrayUtils
import java.lang.Exception
import java.net.Socket

class Client(address: String, port: Int, sniffer: Sniffer) : Thread(){
    var active = true
    private var clientConnected = true
    private val clientSocket = Socket(address,port)
    private val sniffer = sniffer


    private fun isActive(): Boolean {
        return active
    }

    fun isClientConnected(): Boolean {
        return clientConnected
    }

    override fun run() {
        println("Client - Thread started!")
        while(isActive()){
            try {
                handle()
            }catch ( e: Exception ){
                println(e)
                active = false
                clientConnected = false
                try {
                    clientSocket.close()
                } catch( e1: Exception){}
            } finally {
                sleep(10)
            }
        }
        println("Client - Thread is dead!")
    }

    private fun handle() {
        if( clientSocket!!.getInputStream().available() > 0 ){
            sleep(10)
            val bufferIn = ByteArray(clientSocket!!.getInputStream().available())
            clientSocket!!.getInputStream().read(bufferIn)
            sniffer.sendFromClientToServer(bufferIn)
            println("Client - RX: [${ByteArrayUtils.byteToHex(bufferIn)}]")
        }

        val bufferOut = sniffer.receiveFromServer()
        if(bufferOut.isNotEmpty()){
            clientSocket.getOutputStream().write(bufferOut)
        }
    }
}