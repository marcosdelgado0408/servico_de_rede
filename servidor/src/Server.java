import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Marcos V A Delgado Junior
 */


/*
O seridor vai guardar diversos sites -> que serão enviados para
uma aplicação intermediaria e ela vai dizer para o cliente se o site
que ele quer acessar é seguro ou não
*/


public class Server {

    private ServerSocket serverSocket;


    public void newServerSocket(int porta) throws IOException {
        this.serverSocket = new ServerSocket(porta);
    }

    public Socket waitConnection() throws IOException { // vai receber um socket que ele estará escutando
        Socket socket = this.serverSocket.accept();
        return socket;
    }

    public void closeSocket(Socket socket) throws IOException {
        socket.close();
    }


    public static void main(String[] args){


        try {

            Server server = new Server();

            server.newServerSocket(7777);
            System.out.println("Novo server http criado");

            while (true){ // servidor sempre ficar rodando
                System.out.println("esperando cliente.....");
                Socket receber_cliente = server.waitConnection();
                System.out.println("cliente conectado :)");

                BufferedReader buffer = new BufferedReader(new InputStreamReader(receber_cliente.getInputStream()));

                String linha = buffer.readLine();

                String[] requisicoes = linha.split(" ");

                String metodo = requisicoes[0];
                String caminhoArquivo = requisicoes[1];
                String protocolo = requisicoes[2];

                while (!linha.isEmpty()) {
                    System.out.println(linha);
                    linha = buffer.readLine();
                }

                if (caminhoArquivo.equals("/")) { // normalmente navegadores fazem o GET só com o "/"
                    caminhoArquivo = "/index.html";
                }

                File arquivo = new File("src/" + caminhoArquivo);

                String status = protocolo + " 200 OK\r\n";

                if (!arquivo.exists()) {
                    status = protocolo + " 404 Not Found\r\n";
                    arquivo = new File("src/404.html");
                }

                byte[] conteudo = Files.readAllBytes(arquivo.toPath());

                // formatação de uma requisição http
                SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
                formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date data = new Date();
                //Formata a dara para o padrao
                String dataFormatada = formatador.format(data) + " GMT";
                //cabeçalho padrão da output HTTP
                String header = status
                        + "Location: http://localhost:8000/\r\n"
                        + "Date: " + dataFormatada + "\r\n"
                        + "Server: MeuServidor/1.0\r\n"
                        + "Content-Type: text/html\r\n"
                        + "Content-Length: " + conteudo.length + "\r\n"
                        + "Connection: close\r\n"
                        + "\r\n";

                OutputStream output = receber_cliente.getOutputStream();

                output.write(header.getBytes());
                output.write(conteudo);
                output.flush();

                receber_cliente.close(); // quando terminar a comunicação cliente servidor -> matar a comunicação

            }


        }
        catch (IOException e){
            System.out.println("Ocorreu uma falha no tratamento da conexão com o cliente ");
            System.out.println("Erro " + e.getMessage());
        }


    }

}
