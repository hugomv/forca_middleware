import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.Scanner;


public class Cliente {



    public static void main(String[] args) {


        try {
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();


            TProtocol protocol = new TBinaryProtocol(transport);
            Forca.Client client = new Forca.Client(protocol);

            perform(client);


            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }


    }

    private static void perform(Forca.Client client) throws TException {

        idJogador id = client.set_jogador();

        while (!client.estah_Completo()){

            if(client.getVez()==id.getId()){
                Placar placar = client.exibir_rodada(id);
                System.out.print(placar.getPlacar());
                Scanner teclado = new Scanner(System.in);
                Letra letra = new Letra();
                letra.setLetra(teclado.nextLine());
                client.gerar_rodada(letra,id);
            }
        }
    }
}
