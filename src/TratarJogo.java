import java.util.ArrayList;

public class TratarJogo {


    Jogo jogo;
    ArrayList<Jogador> jogadores;
    int id;
    public TratarJogo(){
        id = -1;
        jogo = new Jogo();
        jogadores = new ArrayList<>();
    }

    public String exibirPlacar(){

        return jogo.exibirRodada();
    }

    public int processarRodada(int id,String letra){

        return jogo.processarRodada(jogadores.get(id),letra);
    }

    public int addJogador(){

        jogadores.add(new Jogador(++id));
        return id;
    }

    public boolean isCompleto(){
        return jogo.isCompleto();
    }
}
