import server.Client
import server.Server
import sniffer.Sniffer

fun main() {
    val sniffer = Sniffer()
    val server = Server(3333,sniffer)
    val client = Client("96.232.185.53",510,sniffer)
    server.start()
    client.start()

    while (true){
        Thread.sleep(1000)
//        when {
//            server.isClientConnected() && client.isClientConnected() -> println("ta ok")
//            else -> println("deu ruim")
//        }
    }
}