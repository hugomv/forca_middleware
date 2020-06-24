import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe de interface
 */
public class TratadorJogo implements Forca.Iface{

    Jogo jogo;
    ArrayList<Jogador> jogadores = new ArrayList<>();
    int vez;


    public TratadorJogo(){
        jogo = new Jogo();
        vez = 0;
    }

    public void setVez() {
        if(vez<2){
            this.vez = vez + 1;
        }else {
            vez = 0;
        }

    }


    public Jogador getJogador(int id) {
        return jogadores.get(id);
    }

    public void setJogador(Jogador jogador) {
        jogadores.add(jogador);
    }


    @Override
    public Placar gerar_rodada(Letra letra, idJogador jogador) throws TException {
        jogo.processarRodada(jogadores.get(jogador.getId()),letra.getLetra());

        Placar placar = new Placar();
        String aux = "";
        for(ArrayList<String> palavra_oculta : jogo.getPalavras_ocultas()) {
            aux.concat((palavra_oculta.toString() + "\n"));
        }

        placar.setPlacar(aux);
        setVez();
        return placar;
    }

    @Override
    public Placar exibir_rodada(idJogador jogador) throws TException {

        Placar placar = new Placar();
        StringBuilder aux = new StringBuilder();
        for(ArrayList<String> palavra_oculta : jogo.getPalavras_ocultas()) {
            aux.append((palavra_oculta.toString() + "\n"));
        }
        Random gerador = new Random();
        jogadores.get(jogador.getId()).setPontuacao(gerador.nextInt(1000));
        aux.append(String.format("Pontuacao da roleta: %d \n",jogo.getRoleta()));
        aux.append(String.format("Pontuacao total: %d \n",jogadores.get(jogador.getId()).getPontuacao()));
        aux.append(String.format("Jogador %d, informe uma letra ",jogador.getId()));
        placar.setPlacar(aux.toString());
        return placar;
    }

    @Override
    public idJogador set_jogador() throws TException {
        setJogador(new Jogador(vez));
        idJogador id = new idJogador();
        id.setId(getVez());
        setVez();
        return id;


    }

    @Override
    public boolean estah_Completo() throws TException {
        return jogo.isCompleto();
    }

    @Override
    public int getVez() throws TException {
        return vez;
    }

    public String getPalavras(){
        StringBuilder str = new StringBuilder();
        for(String palavra : jogo.getTrespalavras()){
            str.append(palavra + "\n");
        }
        return str.toString();
    }
}
