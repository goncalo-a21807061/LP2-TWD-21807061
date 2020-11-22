package pt.ulusofona.lp2.theWalkingDEISIGame;

import org.junit.Test;
import java.io.File;


import static org.junit.Assert.assertEquals;

public class TestTWDGameManager {

    @Test
    public void test01Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento humano - andar 2 posicoes
        assertEquals(false , twdGameManager.move(3,3,3,1));

    }


    @Test
    public void test02Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento fora do grafico humano
        assertEquals(false,twdGameManager.move(3,3,3,-1));

    }

    @Test
    public void test03Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste sobreposicao
        assertEquals(false, twdGameManager.move(3,4,3,3));
    }

    @Test
    public void test04Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento fora do grafico zombie
        assertEquals(false, twdGameManager.move(4,4,4,-1));
      
    }
}

