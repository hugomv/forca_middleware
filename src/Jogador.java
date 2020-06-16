public class Jogador {


    int pontuacao;
    int codigo;


    public Jogador(int codigo) {
        pontuacao = 0;
        this.codigo = codigo;
    }


    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = this.pontuacao + pontuacao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
