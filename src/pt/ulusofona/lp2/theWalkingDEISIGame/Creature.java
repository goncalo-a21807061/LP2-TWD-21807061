package pt.ulusofona.lp2.theWalkingDEISIGame;

public abstract class Creature {
    protected int id;
    int idTipo;
    String nome;
    String nomeEquipa;
    String nomePersonagem;
    String foto;
    String local = "jogo";
    int x;
    int y;
    int equipa;
    Equipamento equipamento;
    int idEquipamento;
    int idTipoEquipamento;
    int alcance;
    int equipamentosApanhados = 0;
    boolean moverDiagonal = true;


    public Creature(int id, int idTipo, String nome, int x, int y) {
        this.id = id;
        this.idTipo = idTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.idEquipamento = 0;
    }

    abstract public Boolean getMoverDiagonal();

    abstract public int getX();

    abstract public int getY();

    abstract public void setX(int x);

    abstract public void setY(int y);

    abstract public int getId();

    abstract public int getIdTipo();

    abstract public int getEquipa();

    abstract public String getNome();

    abstract public String getImagePNG();

    abstract public void adicionaEquipamentosEncontrados (int adiciona);

    abstract public int getEquipamentosApanhados();

    abstract public void setEquipmentId(int id);

    abstract public int getIdEquipamento();

    abstract public void setIdTipoEquipamento(int idTipoEquipamento);

    abstract public int getIdTipoEquipamento();

    abstract public int getAlcance();

    abstract public void setLocal(String local);

    abstract public String getLocal();

    abstract public String toString();
}
