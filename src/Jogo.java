import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Jogo{

    private ArrayList<String> palavras = new ArrayList<String>();
    private ArrayList<String> trespalavras = new ArrayList<String >();
    private ArrayList<ArrayList<String>> palavras_ocultas = new ArrayList<ArrayList<String>>();
    private boolean completo;
    private boolean bloqueado;
    private int roleta;

    public Jogo(){

        completo = false;

        sortearPalavras();

    }

    /**
     * Lê do arquivo "lista_de_palavras.txt" e sorteia três palavras
     */
    public void sortearPalavras(){

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
            roleta = gerarRoletaPontuacao();
            jogador.setPontuacao(roleta);

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

    /**
     * Exibir o placar de forma simplificada
     * @return
     */
    public String exibirRodada(){

        StringBuilder saida = new StringBuilder();

        for(ArrayList<String> palavra_oculta : palavras_ocultas) {
            saida.append((palavra_oculta) + "\n");
        }

        return saida.toString();

    }


    public ArrayList<String> getTrespalavras() {
        return trespalavras;
    }

    public void setTrespalavras(ArrayList<String> trespalavras) {
        this.trespalavras = trespalavras;
    }

    public ArrayList<ArrayList<String>> getPalavras_ocultas() {
        return palavras_ocultas;
    }

    public void setPalavras_ocultas(ArrayList<ArrayList<String>> palavras_ocultas) {
        this.palavras_ocultas = palavras_ocultas;
    }


    public int getRoleta() {
        return roleta;
    }

    public boolean isCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }




}
