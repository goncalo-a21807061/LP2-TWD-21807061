package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano {
    int id;
    int idTipo = 1;
    String nome;
    String nomeEquipa = "Os Vivos";
    int x;
    int y;
    int equipa = 0;
    int equipamentosApanhados = 0;
    Equipamento equipamento;


    public Humano(int id, int idTipo, String nome, int x, int y) {
        this.id = id;
        this.idTipo = idTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public String getNome() { return nome; }

    public String getImagePNG() {
        return "human.png";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return id + " | " + "Humano" + " | " + nomeEquipa + " | " + nome + " " + equipamentosApanhados + " @ (" + x + ", " + y + ")";
    }

    public int getEquipa() {
        return equipa;
    }

    public void adicionaEquipamentosEncontrados (int adiciona) {
        equipamentosApanhados += adiciona;
    }

    public int getEquipamentosApanhados() {
        return equipamentosApanhados;
    }

}
