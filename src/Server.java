import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String args[]){
        try {
            ServerSocket server = new ServerSocket(3322);
            System.out.println("Servidor iniciado na porta 3322");

            Socket cliente = server.accept();
            System.out.println("Cliente conectado do IP "+cliente.getInetAddress().
                    getHostAddress());
            Scanner entrada = new Scanner(cliente.getInputStream());
            while(entrada.hasNextLine()){
                System.out.println(entrada.nextLine());
            }

            entrada.close();
            server.close();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
