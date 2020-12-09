package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Equipamento {
    int id;
    int idTipo;
    int x;
    int y;
    int duracao;
    String titulo;
    String descricao;
    String defensivoOuOfensivo;
    String foto;


    public Equipamento(int id, int idTipo, int x, int y) {
        this.id = id;
        this.idTipo = idTipo;
        this.x = x;
        this.y = y;
        if(idTipo == 0) {
            defensivoOuOfensivo = "Defensivo";
            titulo = "Escudo de madeira";
            descricao = "Permite obter protecção contra 1 ataque de zombies.\n\nApós esse ataque, o escudo é destruído.";
            foto = "equipment_0.png";
            duracao = 1;
        } else if(idTipo == 1) {
            defensivoOuOfensivo = "Ofensivo";
            titulo = "Espada Hattori Hanzo";
            descricao = "Serve para decapitar e/ou desmembrar zombies.";
            foto = "equipment";
            duracao = 100;
        } else if(idTipo == 2) {
            defensivoOuOfensivo = "Ofensivo";
            titulo = "Pistola Walther PPK";
            descricao = "A pistola tem 3 balas, por isso permite matar 3 Zombies.\n\nA pistola não tem efeito contra Zombies Vampiros.\n\n" +
                    "Quando as balas se esgotarem, a pistola deixa de ter efeito.";
            foto = "gun.png";
            duracao = 3;
        } else if(idTipo == 3) {
            defensivoOuOfensivo = "Defensivo";
            titulo = "Escudo táctico";
            descricao = "Permite obter protecção contra vários ataques.";
            foto = "tactical_shield.png";
            duracao = 100;
        } else if(idTipo == 4) {
            defensivoOuOfensivo = "Defensivo";
            titulo = "Revista Maria\n\n(para enrolar no braço)";
            descricao = "Permite obter protecção contra ataques de Zombies Idosos.\n\nNão protege contra ataques de outros Zombies.";
            foto = "rolled_magazine.png";
            duracao = 100;
        } else if(idTipo == 5) {
            defensivoOuOfensivo = "Defensivo";
            titulo = "Cabeça de Alho";
            descricao = "Funciona como repelente e permite obter protecção contra ataques de Zombies Vampiros.";
            foto = "garlic.png";
            duracao = 100;
        } else if(idTipo == 6) {
            defensivoOuOfensivo = "Ofensivo";
            titulo = "Estaca de Madeira";
            descricao = "Permite matar Zombies.\\n\\nInclui Zombies Vampiros.";
            foto = "equipment_0.png";
            duracao = 100;
        } else if(idTipo == 7) {
            defensivoOuOfensivo = "Defensivo";
            titulo = "Garrafa de lixívia (1 litro)";
            descricao = "Permite obter protecção contra ataques de Zombies, pois o Zombies não sente o cheiro do vivo.\\n\\n Para uma defesa ser bem sucedida, são necessários/gastos 0.3 litros de lixívia.";
            foto = "bleach.png";
            duracao = 3;
        } else if(idTipo == 8) {
            defensivoOuOfensivo = "Defensivo";
            titulo = "Veneno";
            descricao = "O veneno dá protecção durante 2 turnos. Os turnos começam a contar logo a seguir ao turno em que o Vivo consome o veneno.\\n\\nSe o “Vivo” estiver envenenado durante 3 turnos, morre.";
            foto = "poison.png";
            duracao = 3;
        } else if(idTipo == 9) {
            defensivoOuOfensivo = "Defensivo";
            titulo = "Antidoto (para os envenenados)";
            descricao = "O antídoto cura um “Vivo” envenenado.\\n\\nApenas podem apanhar o antídoto os “vivos” que estejam envenenados.\\n\\nAo ser apanhado, o antídoto é consumido e desaparece automaticamente do jogo.";
            foto = "antidote.png";
            duracao = 100;
        } else if(idTipo == 10) {
            defensivoOuOfensivo = "Defensivo/Ofensivo";
            titulo = "Beskar helmet";
            descricao = "Este fantástico capacete futurista não só defende quem o usa, como também serve como arma, dado que multiplica a força das cabeçadas por um número muito grande.";
            foto = "beskar_helmet.png";
            duracao = 100;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getImagePNG() {
        return foto;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDuracao(int duracao) {
        this.duracao -= duracao;
    }

    public int getDuracao() {
        return duracao;
    }
}
