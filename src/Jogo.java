import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Jogo {

    private ArrayList<String> palavras = new ArrayList<String>();
    private ArrayList<String> trespalavras = new ArrayList<>();
    private ArrayList<ArrayList<String>> palavras_ocultas = new ArrayList<>();
    private boolean completo;

    public Jogo(){

        completo = false;

        //ler o arquivo
        try {
            FileReader arq = new FileReader("lista_de_palavras.txt");
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine();
            palavras.add(linha);
            while (linha != null) {
                linha = lerArq.readLine();
                if(linha !=null) palavras.add(linha.toLowerCase());

            }

            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sortear a palavra
        Random gerador = new Random();
        trespalavras.add(palavras.get(gerador.nextInt(palavras.size())-1));
        trespalavras.add(palavras.get(gerador.nextInt(palavras.size())-1));
        trespalavras.add(palavras.get(gerador.nextInt(palavras.size())-1));

//        palavra = "amor";
        for(String palavra : trespalavras) {
            ArrayList<String> palavra_oculta = new ArrayList<>();
            for (int i = 0; i < palavra.length(); i++) {
                palavra_oculta.add("_");
            }
            palavras_ocultas.add(palavra_oculta);
        }

    }

    /**
     * Exibe o prompt para que o jogador informe a próxima letra
     * @param jogador = identificador do jogador
     * @param cliente1
     * @return = letra escolhida pelo jogador
     */
    public String exibirRodada(Jogador jogador, Socket cliente1){
        for(ArrayList<String> palavra_oculta : palavras_ocultas) {
            System.out.println(palavra_oculta);
        }

        Scanner scanner = new Scanner(System.in);

        //cliente1.getOutputStream(String.format("Jogador %d, informe uma letra ",jogador.getCodigo()));
        String letra = scanner.next();
        return letra;
    }

    /**
     * Analisa se a letra informada consta na palavra
     * @param   letra   palavra informada pelo usuário
*               jogador identificador do jogador
     * @param cliente
     * @return      0 se constar; -1 se não; 1 se palavra completa
     */
    public int processarRodada(Jogador jogador, String letra, Socket cliente){

        if(trespalavras.get(0).contains(letra) || trespalavras.get(1).contains(letra) || trespalavras.get(2).contains(letra)){

            jogador.setPontuacao(gerarRoletaPontuacao());

            for(int k = 0; k < 3; k++) {
                String palavra = trespalavras.get(k);

                int i = 0;
                while (i < palavra.length() && palavra.substring(i).contains(letra)) {
                    i = palavra.substring(i).indexOf(letra) + i;
                    palavras_ocultas.get(k).set(i, letra);
                    i++;

                }
            }

            //ver se a palavra está completa
            if(!palavras_ocultas.get(0).contains("_") && !palavras_ocultas.get(1).contains("_") && !palavras_ocultas.get(2).contains("_")){
                completo = true;
                return 1;
            }else {
                return 0;
            }

        }else{
            return -1;
        }
    }

    /**
     * Girar a roleta
     * @return A pontuaćão sorteada
     */
    private int gerarRoletaPontuacao(){
        Random gerador = new Random();
        return gerador.nextInt(1000);

    }



}
