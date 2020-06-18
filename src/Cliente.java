import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Cliente {



    public static void main(String[] args) {

        Socket cliente;

        try {
            cliente = new Socket("127.0.0.1",3322);
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            while (cliente.isConnected()){
                String inputLine;
                while ((inputLine = in.readLine()) == null) {
                    //aguarda até receber input
                }
                System.out.println(inputLine);
                System.out.println(in.readLine());
                System.out.println(in.readLine());
                System.out.println(in.readLine());


                //in.close();
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                Scanner teclado = new Scanner(System.in);
                String letra = teclado.nextLine();
                out.println(letra);
                out.flush();
                //out.close();

            }


        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
