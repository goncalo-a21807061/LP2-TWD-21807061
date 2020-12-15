package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano extends Creature {

    public Humano(int id, int idTipo, String nome, int x, int y) {
        super(id, idTipo, nome, x, y);
        this.idEquipamento = 0;
        if (idTipo == 5) {
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
            moverDiagonal = false;
        } else if(idTipo == 9) {
            nome = "Cão";
            equipa = 10;
            alcance = 2;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        }
    }
    public Boolean getMoverDiagonal() {
        return moverDiagonal;
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

    public int getIdTipo() { return idTipo; }

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

    public void setIdTipoEquipamento(int idTipoEquipamento) {
        this.idTipoEquipamento = idTipoEquipamento;
    }

    public int getIdTipoEquipamento() {
        return idTipoEquipamento;
    }

    public int getAlcance() { return alcance; }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    @Override
    public String toString() {
        if (local == "safe haven") {
            return id + " | " + idTipo + " | " + nomeEquipa + " | " + nome + " | " + equipamentosApanhados + " @ (A salvo)" ;
        } else if (local == "morta") {
            return id + " | " + idTipo + " | " + nomeEquipa + " | " + nome + " | " + equipamentosApanhados + " @ (RIP)" ;
        } else {
            return id + " | " + idTipo + " | " + nomeEquipa + " | " + nome + " | " + equipamentosApanhados + " @ (" + x + ", " + y + ")";
        }
    }
}
