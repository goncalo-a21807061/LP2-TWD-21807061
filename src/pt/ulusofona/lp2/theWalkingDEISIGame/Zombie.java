package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Zombie extends Creature {

    public Zombie(int id, int idTipo, String nome, int x, int y) {
        super(id, idTipo, nome, x, y);
        this.idEquipamento = 0;
        if(idTipo == 0) {
            nomePersonagem = "Criança (Zombie)";
            equipa = 20;
            alcance = 1;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if(idTipo == 1) {
            nomePersonagem = "Adulto (Zombie)";
            equipa = 20;
            alcance = 2;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if (idTipo == 2) {
            nomePersonagem = "Militar (Zombie)";
            equipa = 20;
            alcance = 3;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
        } else if (idTipo == 3) {
            nomePersonagem = "Idoso (Zombie)";
            equipa = 20;
            alcance = 1;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
            moverDiagonal = false;
        } else if (idTipo == 4) {
            nomePersonagem = "Zombie Vampiro";
            equipa = 20;
            alcance = 2;
            nomeEquipa = "Os Outros";
            foto = "zombie.png";
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

    public void setEquipa(int equipa) {
        this.equipa = equipa;
    }

    public void setNomeEquipa(String equipa) {
        this.nomeEquipa = nomeEquipa;
    }

    public void setImagePNG(String image) {
        this.foto = image;
    }

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
            return id + " | " + nomePersonagem + " | " + nomeEquipa + " | " + nome + " " + equipamentosApanhados + " @ (A salvo)" ;
        } else if (local == "morta") {
            return id + " | " + nomePersonagem + " | " + nomeEquipa + " | " + nome + " " + equipamentosApanhados + " @ (RIP)" ;
        } else {
            return id + " | " + nomePersonagem + " | " + nomeEquipa + " | " + nome + " " + equipamentosApanhados + " @ (" + x + ", " + y + ")";
        }
    }

}
