package security.checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Victor
 */

// Classe que trata a conexão
public class ConnectionTreatment implements Runnable {
    private Socket socket;
    private String request = null;
    private String response = null;
    private OutputStream output;
    
    public ConnectionTreatment(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // recebe a request e armazena
        request = getRequest();
        
        // verifica se recebeu a request corretamente
        if (!request.equals("")) {
            System.out.println("Request recebido!");
        } else {
            System.out.println("Request não recebido!");
            System.exit(1);
        }
        
        Client client = new Client(request);
        
        // recebe o response do servidor e armazena
        try {
            response = client.getResponse();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTreatment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            output = socket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTreatment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // verifica se a página enviada pelo servidor é segura
        response = security(response);
        
        try {
            output.write(response.getBytes());
            output.flush();
            System.out.println("Response enviado com sucesso!");
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTreatment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // Escuta o request enviado pelo cliente e retorna ele no formato de String
    private String getRequest() {
        String stringRequest = "";
        //cria um BufferedReader a partir do InputStream do cliente
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTreatment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Lê a primeira linha
        String linha = null;
        try {
            linha = buffer.readLine();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTreatment.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Enquanto a linha não for vazia
        while (!linha.isEmpty()) {
            //imprime a linha
            stringRequest += linha+"\r\n";
            try {
                //lê a proxima linha
                linha = buffer.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ConnectionTreatment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return stringRequest;
    }
    
    private String security(String response) {
        if (response.contains("window.location.href")) {
            System.out.println("Script malicioso encontrado!");
            System.out.println("Retirando script malicioso...");
            response = response.replace("window.location.href", "//window.location.href");
            return response;
        }
        return response;
    }
}
