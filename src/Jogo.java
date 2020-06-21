import org.apache.thrift.TException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Jogo implements Forca.Iface{

    private ArrayList<String> palavras = new ArrayList<String>();
    private ArrayList<String> trespalavras = new ArrayList<String >();
    private ArrayList<ArrayList<String>> palavras_ocultas = new ArrayList<ArrayList<String>>();
    private boolean completo;
    private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();

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
            ArrayList<String> palavra_oculta = new ArrayList<String>();
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
    public String exibirRodada(Jogador jogador, Socket cliente1) throws IOException {

        PrintWriter out = new PrintWriter(cliente1.getOutputStream(), true);
        StringBuilder saida = new StringBuilder();

        for(ArrayList<String> palavra_oculta : palavras_ocultas) {
            out.println((palavra_oculta));
        }
        out.println(String.format("Jogador %d, informe uma letra ",jogador.getCodigo()));
        out.flush();
        String letra;

        BufferedReader in = new BufferedReader(new InputStreamReader(cliente1.getInputStream()));

        while ((letra = in.readLine()) == null){
            //aguardando input do jogador
        }

        return letra;
    }

    /**
     * Analisa se a letra informada consta na palavra
     * @param   letra   palavra informada pelo usuário
*               jogador identificador do jogador
     * @param   jogador
     *          letra
     * @return      0 se constar; -1 se não; 1 se palavra completa
     */
    public int processarRodada(Jogador jogador, String letra){

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


    @Override
    public Placar gerar_rodada(Letra letra, idJogador jogador) throws TException {
        processarRodada(jogadores.get(jogador.getId()),letra.getLetra());

        Placar placar = new Placar();
        String aux = "";
        for(ArrayList<String> palavra_oculta : palavras_ocultas) {
            aux.concat((palavra_oculta.toString() + "\n"));
        }

        placar.setPlacar(aux);
        return placar;
    }

    @Override
    public Placar exibir_rodada(idJogador jogador) throws TException {

        Placar placar = new Placar();
        String aux = "";
        for(ArrayList<String> palavra_oculta : palavras_ocultas) {
            aux.concat((palavra_oculta.toString() + "\n"));
        }
       aux.concat(String.format("Jogador %d, informe uma letra ",jogador.getId()));
       Random gerador = new Random();
       jogadores.get(jogador.getId()).setPontuacao(gerador.nextInt(1000));
       aux.concat(String.format("Pontuacao da roleta: %d \n",jogadores.get(jogador.getId()).getPontuacao()));

       placar.setPlacar(aux);
       return placar;
    }



    public Jogador getJogador(int id) {
        return jogadores.get(id);
    }

    public void setJogador(Jogador jogador) {
        jogadores.add(jogador);
    }


}
