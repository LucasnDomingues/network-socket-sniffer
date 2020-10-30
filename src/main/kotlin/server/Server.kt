package server

import sniffer.Sniffer
import utils.ByteArrayUtils
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

class Server(port: Int, sniffer: Sniffer) : Thread(){
    var active = true
    private var clientConnected = false
    private val serverSocket = ServerSocket(port)
    private var clientSocket: Socket? = null
    private val sniffer = sniffer


    private fun isActive(): Boolean {
        return active
    }

    fun isClientConnected(): Boolean {
        return clientConnected
    }

    override fun run() {
        println("Server - Thread started!")
        while(isActive()){
            try {
                handle()
            }catch ( e: Exception ){
                println(e)
                active = false
                clientConnected = false
                try {
                    clientSocket?.close()
                } catch( e1: Exception){}
            } finally {
                sleep(10)
            }
        }
        println("Server - Thread is dead!")
    }

    private fun handle() {
        if(!isClientConnected()){
            println("Server - Going to wait client")
            clientSocket = serverSocket.accept()
            clientConnected = true
            println("Server - Someone Connected")
        } else {
            if( clientSocket!!.getInputStream().available() > 0 ){
                sleep(10)
                val bufferIn = ByteArray(clientSocket!!.getInputStream().available())
                clientSocket!!.getInputStream().read(bufferIn)
                sniffer.sendFromServerToClient(bufferIn)
                println("Server - RX: [${ByteArrayUtils.byteToHex(bufferIn)}]")
            }

            val bufferOut = sniffer.receiveFromClient()
            if(bufferOut.isNotEmpty()){
                clientSocket!!.getOutputStream().write(bufferOut)
            }
        }
    }
}