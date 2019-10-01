package security.checker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author João Victor
 */


// Classe responsável por receber as requisições http e instanciar a classe de tratamento da conexão
public class Server {
    public static void main(String[] args) throws IOException {
        
        ServerSocket server = new ServerSocket(1234);
        
        while (true) {
            Socket socket = server.accept();
            //verifica se esta conectado  
            if (socket.isConnected()) {
                //imprime na tela o IP do cliente
                System.out.println("Nova conexão: "+ socket.getInetAddress());
            }
            new Thread(new ConnectionTreatment(socket)).start();
        }
    }
}
