import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {

        Jogador jogador1 = new Jogador(1);
        Jogador jogador2 = new Jogador(2);
        Jogador jogador3 = new Jogador(3);

        Jogo jogo = new Jogo();



        try {
            ServerSocket server = new ServerSocket(3322);
            System.out.println("Servidor iniciado na porta 3322");


            Socket cliente1 = server.accept();
            System.out.println("Jogador 1 conectado do IP "+cliente1.getInetAddress().
                    getHostAddress());

            Socket cliente2 = server.accept();
            System.out.println("Jogador 2 conectado do IP "+cliente2.getInetAddress().
                    getHostAddress());

            Socket cliente3 = server.accept();
            System.out.println("Jogador 3 conectado do IP "+cliente3.getInetAddress().
                    getHostAddress());

            int i = 0;
            while (true){

                String letra = jogo.exibirRodada(jogador1,cliente1);
                i = jogo.processarRodada(jogador1, letra,cliente1);

                if(i==1)break;

                letra = jogo.exibirRodada(jogador2, cliente2);
                i = jogo.processarRodada(jogador2, letra, cliente2);

                if(i==1)break;

                letra = jogo.exibirRodada(jogador3, cliente3);
                i = jogo.processarRodada(jogador3, letra, cliente3);

                if(i==1)break;
            }

            System.out.println("Jogo finalizado! Placar:");
            System.out.println(String.format("Jogador 1 /t Pontuacao: %d",jogador1.getPontuacao()));
            System.out.println(String.format("Jogador 2 /t Pontuacao: %d",jogador2.getPontuacao()));
            System.out.println(String.format("Jogador 3 /t Pontuacao: %d",jogador3.getPontuacao()));

            server.close();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }










    }
}
