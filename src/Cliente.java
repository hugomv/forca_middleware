import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {


    public static void main(String[] args) {

        Socket cliente;

        try {
            cliente = new Socket("127.0.0.1",3322);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
