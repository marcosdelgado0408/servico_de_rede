package security.checker;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author João Victor
 */
public class Client {
    private String request;
    
    public Client(String request) {
        this.request = request;
    }
    
    // Faz a conexão com o servidor e envia o request
    private Socket connect() throws IOException {
        String[] host = getHost();
        //cria um socket
        Socket socket = new Socket(host[0],80);
        //verifica se está conectado
        if (socket.isConnected()) {
            System.out.println("Conexão com servidor externo ("+socket.getInetAddress()+") realizada com sucesso!");
            System.out.println("Aguardando resposta do servidor externo... ");
        }
        
        
        //  OutputStream para enviar a requisição
        OutputStream output = socket.getOutputStream();
        
        // Transformando a string do request em um vetor de bytes
        byte[] requisicaoBytes = request.getBytes();
        
        // Escreve o vetor de bytes no canal de envio
        output.write(requisicaoBytes);
        
        // Informa que finalizou a escrita
        output.flush();
        
        return socket;
    }
    
    // 
    public String getResponse() throws IOException {
        String response = "";
        Socket socket = connect();
        
        // cria um scanner a partir do InputStream do servidor
        Scanner input = new Scanner(socket.getInputStream());
        while (input.hasNext()) {
            //salva o response na String
            response += input.nextLine()+"\r\n";
        }
        return response;
    }
    
    // Retorna um array com o host e a porta
    private String[] getHost() {
        String[] array = request.split("\r\n");
        return (array[1].split(" ")[1]).split(":");
    }
}
