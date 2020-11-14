package pt.ulusofona.lp2.theWalkingDEISIGame;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class TestTWDGameManager {

    @Test
    public void test01Move() {
        //teste movimento normal humano
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));
        //ver se Humano com ID 1 está na posição (3,3)
        assertEquals(1,twdGameManager.getElementId(3,3));
        //mover Humano da posição (3,3) para a posicao (3,2)
        twdGameManager.move(3,3,3,2);
        //Verificar se Humano se moveu
        assertEquals(1,twdGameManager.getElementId(3,2));
    }


    @Test
    public void test02Move() {
        //teste movimento fora do grafico humano
    }

    @Test
    public void test03Move() {
        //teste movimento normal zombie
    }

    @Test
    public void test04Move() {
        //teste movimento fora do grafico zombie
    }


    //se der para implementar o Equipamento testar se o humano apanha o equipamento ou nao etc
}
