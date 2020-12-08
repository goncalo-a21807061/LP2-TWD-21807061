package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Creature {
    int id;
    int idTipo;
    String nome;
    String nomeEquipa;
    String foto;
    int x;
    int y;
    int equipa;
    Equipamento equipamento;
    int idEquipamento;
    int alcance;
    int equipamentosApanhados = 0;


    public Creature(int id, int idTipo, String nome, int x, int y) {
        this.id = id;
        this.idTipo = idTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.idEquipamento = 0;
        if(idTipo == 0) {
            nome = "Criança (Zombie)";
            equipa = 20;
            alcance = 1;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if(idTipo == 1) {
            nome = "Adulto (Zombie)";
            equipa = 20;
            alcance = 2;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if (idTipo == 2) {
            nome = "Militar (Zombie)";
            equipa = 20;
            alcance = 3;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if (idTipo == 3) {
            nome = "Idoso (Zombie)";
            equipa = 20;
            alcance = 1;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if (idTipo == 4) {
            nome = "Zombie Vampiro";
            equipa = 20;
            alcance = 2;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if (idTipo == 5) {
            nome = "Criança (Vivo)";
            equipa = 10;
            alcance = 1;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        } else if (idTipo == 6) {
            nome = "Adulto (Vivo)";
            equipa = 10;
            alcance = 2;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        } else if (idTipo == 7) {
            nome = "Militar (Vivo)";
            equipa = 10;
            alcance = 3;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        } else if (idTipo == 8) {
            nome = "Idoso (Vivo)";
            equipa = 10;
            alcance = 1;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        } else if(idTipo == 9) {
            nome = "Cão";
            equipa = 10;
            alcance = 2;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        }
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() { return id; }

    public int getEquipa() { return equipa; }

    public String getNome() { return nome; }

    public String getImagePNG() { return foto; }

    public void adicionaEquipamentosEncontrados (int adiciona) {
        equipamentosApanhados += adiciona;
    }

    public int getEquipamentosApanhados() {
        return equipamentosApanhados;
    }

    public void setEquipmentId(int id) {
        this.idEquipamento = id;
    }

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public int getAlcance() { return alcance; }

    @Override
    public String toString() {
        return id + " | " + idTipo + " | " + nomeEquipa + " | " + nome + " | " + equipamentosApanhados + " @ (" + x + ", " + y + ")";
    }
}
