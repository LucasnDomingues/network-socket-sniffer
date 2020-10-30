package sniffer

class Sniffer {
    var serverRx = ByteArray(0)
    var clientRx = ByteArray(0)

    fun sendFromServerToClient(buffer: ByteArray){
        clientRx = clientRx.plus(buffer)
    }

    fun sendFromClientToServer(buffer: ByteArray){
        serverRx = serverRx.plus(buffer)
    }

    fun receiveFromServer(): ByteArray{
        val buffer = clientRx
        clientRx = ByteArray(0)
        return buffer
    }

    fun receiveFromClient(): ByteArray{
        val buffer = serverRx
        serverRx = ByteArray(0)
        return buffer
    }

}