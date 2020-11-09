package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Equipamento {
    int id;
    int idTipo;
    int x;
    int y;


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

    public String getTitulo(){
        if(id == 0) {
            return "Escudo de madeira";
        } else {
            return "Espada samurai";
        }
    }

    public String getDescriçao() {
        if(id == 0) {
            return "Permite obter protecção contra 1 ataque de zombies.\n" + "Após esse ataque, é destruído.";
        }
        else {
            return "Serve para decapitar e/ou desmembrar zombies.";
        }

    }

}
