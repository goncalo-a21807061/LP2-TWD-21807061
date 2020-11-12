package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Zombie {
    int id;
    int idTipo = 0;
    String nome;
    String nomeEquipa = "Os Outros";
    int x;
    int y;
    int equipa = 1;
    int equipamentosDestruidos = 0;

    public Zombie(int id, int idTipo, String nome, int x, int y) {
        this.id = id;
        this.idTipo = idTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
    }

    public Zombie(int id, int idTipo) {
        this.id = id;
        this.idTipo = idTipo;
    }

    //falta construtor


    public int getId() {
        return id;
    }

    public String getImagePNG() {
        return "zombie.png";
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public String toString() {
        return id + " | " + idTipo + " | " + nomeEquipa + " | " + nome + " | " + equipamentosDestruidos + " @ (" + x + ", " + y + ") ";
    }

    public int getEquipa() {
        return equipa;
    }

    public void adicionaEquipamentosDestruidos (int adiciona) {
        equipamentosDestruidos += adiciona;
    }
}
