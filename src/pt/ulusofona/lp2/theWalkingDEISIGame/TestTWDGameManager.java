package pt.ulusofona.lp2.theWalkingDEISIGame;

import org.junit.Test;

import java.io.File;


import static org.junit.Assert.assertEquals;

public class TestTWDGameManager {

    @Test
    public void test01Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));
        //teste movimento normal humano

        assertEquals(true, twdGameManager.move(3,3,3,2));

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

        //teste movimento normal zombie
        assertEquals(true, twdGameManager.move(4,4,4,3));
    }

    @Test
    public void test04Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento fora do grafico zombie
        assertEquals(false, twdGameManager.move(4,4,4,-1));
      
    }
}

