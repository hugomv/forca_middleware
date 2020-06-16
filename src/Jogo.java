import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Jogo {

    private ArrayList<String> palavras = new ArrayList<String>();
    private String palavra;
    private ArrayList<String> palavra_oculta = new ArrayList<>();
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
        palavra = palavras.get(gerador.nextInt(palavras.size())-1);
//        palavra = "amor";
        for(int i = 0; i < palavra.length();i++){
            palavra_oculta.add("_");

        }

    }

    /**
     * Exibe o prompt para que o jogador informe a próxima letra
     * @param jogador = identificador do jogador
     * @return = letra escolhida pelo jogador
     */
    public String exibirRodada(Jogador jogador){
        System.out.println(palavra_oculta);

        Scanner scanner = new Scanner(System.in);

        System.out.print(String.format("Jogador %d, informe uma letra ",jogador.getCodigo()));
        String letra = scanner.next();
        return letra;
    }

    /**
     * Analisa se a letra informada consta na palavra
     * @param   letra   palavra informada pelo usuário
*               jogador identificador do jogador
     * @return      0 se constar; -1 se não; 1 se palavra completa
     */
    public int processarRodada(Jogador jogador, String letra){

        if(palavra.contains(letra)){

            int i = 0;
            jogador.setPontuacao(gerarRoletaPontuacao());
            while(i < palavra.length() && palavra.substring(i).contains(letra)){
                i = palavra.substring(i).indexOf(letra) + i;
                palavra_oculta.set(i,letra);
                i++;

            }
            //ver se a palavra está completa
            if(!palavra_oculta.contains("_")){
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
