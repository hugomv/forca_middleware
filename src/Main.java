public class Main {

    public static void main(String[] args) {


        Jogador jogador1 = new Jogador(1);
        Jogador jogador2 = new Jogador(2);
        Jogador jogador3 = new Jogador(3);

        Jogo jogo = new Jogo();

        int i = 0;
        while (true){

            String letra = jogo.exibirRodada(jogador1);
            i = jogo.processarRodada(jogador1, letra);

            if(i==1)break;

            letra = jogo.exibirRodada(jogador2);
            i = jogo.processarRodada(jogador2, letra);

            if(i==1)break;

            letra = jogo.exibirRodada(jogador3);
            i = jogo.processarRodada(jogador3, letra);

            if(i==1)break;
        }

        System.out.println("Jogo finalizado! Placar:");
        System.out.println(String.format("Jogador 1 /t Pontuacao: %d",jogador1.getPontuacao()));
        System.out.println(String.format("Jogador 2 /t Pontuacao: %d",jogador2.getPontuacao()));
        System.out.println(String.format("Jogador 3 /t Pontuacao: %d",jogador3.getPontuacao()));



    }
}
