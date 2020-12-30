package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano extends Creature {


    public Humano(int id, int idTipo, String nome, int x, int y) {
        super(id, idTipo, nome, x, y);
        this.idEquipamento = 0;
        if (idTipo == 5) {
            nomePersonagem = "Criança (Vivo)";
            equipa = 10;
            alcance = 1;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        } else if (idTipo == 6) {
            nomePersonagem = "Adulto (Vivo)";
            equipa = 10;
            alcance = 2;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        } else if (idTipo == 7) {
            nomePersonagem = "Militar (Vivo)";
            equipa = 10;
            alcance = 3;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        } else if (idTipo == 8) {
            nomePersonagem = "Idoso (Vivo)";
            equipa = 10;
            alcance = 1;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
            moverDiagonal = false;
        } else if(idTipo == 9) {
            nomePersonagem = "Cão";
            equipa = 10;
            alcance = 2;
            nomeEquipa = "Os Vivos";
            foto = "human.png";
        }
    }

    @Override
    public Boolean getMoverDiagonal() {
        return moverDiagonal;
    }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getId() { return id; }

    @Override
    public int getIdTipo() { return idTipo; }

    @Override
    public int getEquipa() { return equipa; }

    @Override
    public void setEquipa(int equipa) {
        this.equipa = equipa;
    }

    @Override
    public void setNomeEquipa(String equipa) {
        this.nomeEquipa = equipa;
    }

    @Override
    public void setImagePNG(String image) {
        this.foto = image;
    }

    @Override
    public String getNome() { return nome; }

    @Override
    public String getImagePNG() { return foto; }

    @Override
    public void adicionaEquipamentosEncontrados (int adiciona) {
        equipamentosApanhados += adiciona;
    }

    @Override
    public int getEquipamentosApanhados() {
        return equipamentosApanhados;
    }

    @Override
    public void setEquipmentId(int id) {
        this.idEquipamento = id;
    }

    @Override
    public int getIdEquipamento() {
        return idEquipamento;
    }

    @Override
    public void setIdTipoEquipamento(int idTipoEquipamento) {
        this.idTipoEquipamento = idTipoEquipamento;
    }

    @Override
    public int getIdTipoEquipamento() {
        return idTipoEquipamento;
    }

    @Override
    public int getAlcance() { return alcance; }

    @Override
    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String getLocal() {
        return local;
    }

    @Override
    public void colocaAZeroEquipamentos() {
        equipamentosApanhados = 0;
    }

    @Override
    public void humanoParaZombie() {
        if(nomePersonagem == "Criança (Vivo)") {
            nomePersonagem = "Criança (Zombie)";
        } else if (nomePersonagem == "Adulto (Vivo)") {
            nomePersonagem = "Adulto (Zombie)";
        } else if(nomePersonagem == "Militar (Vivo)") {
            nomePersonagem = "Militar (Zombie)";
        } else if(nomePersonagem == "Idoso (Vivo)") {
            nomePersonagem = "Idoso (Zombie)";
        }
    }

    @Override
    public void setEnvenenado(boolean envenenado) {
        this.envenenado = envenenado;
    }

    @Override
    public boolean getEnvenenado() {
        return envenenado;
    }

    @Override
    public String toString() {
        if (local == "safe haven") {
            return id + " | " + nomePersonagem + " | " + nomeEquipa + " | " + nome + " " + equipamentosApanhados + " @ A salvo" ;
        } else if (local == "morta") {
            return id + " | " + nomePersonagem + " | " + nomeEquipa + " | " + nome + " " + equipamentosApanhados + " @ (RIP)" ;
        } else {
            return id + " | " + nomePersonagem + " | " + nomeEquipa + " | " + nome + " " + equipamentosApanhados + " @ (" + x + ", " + y + ")";
        }
    }
}
