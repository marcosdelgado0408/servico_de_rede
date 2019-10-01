import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        try{


           Socket socket = new Socket("google.com", 80);

           OutputStream output = socket.getOutputStream();
           Scanner receber_mensagem = new Scanner(socket.getInputStream());

         //  Scanner scanner = new Scanner(System.in);
           System.out.print("Digite o site para saber se ele é seguro ou não: ");
           //String nome_site = scanner.nextLine();
           System.out.print("\n");

           String requisicao = "" + "GET / Http/1.1"  + "Host: google.com"+"\r\n\r\n";

           byte[] requisicao_bytes = requisicao.getBytes();

           output.write(requisicao_bytes);
           output.flush();

           System.out.println("Mensagem recebida do servidor: \n  ");
           while (receber_mensagem.hasNext()){ // vai receber mensagem ou algum texto ou html, etc do servidor
              System.out.println(receber_mensagem.nextLine());
           }



           output.close();
           //scanner.close();
           receber_mensagem.close();
           socket.close();

       }
        catch (IOException e){ // tratando exceções de entrada e saida do cliente
           System.out.println("Erro no cliente " + e);
           e.printStackTrace();
       }


    }

}
