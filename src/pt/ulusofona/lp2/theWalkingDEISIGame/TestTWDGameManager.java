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

        //teste humano ataca Zombie
        assertEquals(true, twdGameManager.move(3,4,3,3));
    }

    @Test
    public void test04Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento fora do grafico zombie
        assertEquals(false, twdGameManager.move(4,4,4,-1));
      
    }

    @Test
    public void test05Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste movimento humano safe haven
        assertEquals(true,twdGameManager.move(5,5,6,6));
    }

    @Test
    public void test06Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste movimento zombie safe haven
        assertEquals(false,twdGameManager.move(5,4,6,6));
    }

    @Test
    public void test07Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste movimento humano apanhar a espada
        assertEquals(true,twdGameManager.move(2,2,2,3));
    }

    @Test
    public void test08GetInitialTeam() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste equipa inicial
        assertEquals(10,twdGameManager.getInitialTeam());
    }

    @Test
    public void test09popCultureExtravaganza() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));
        String[] strings = new String[0];

        //teste funçao popCultureExtravaganza -> retornar lista vazia
        assertEquals(strings,twdGameManager.popCultureExtravaganza());
    }

    @Test
    public void test10isDoorToSafeHaven() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste verificar se é safe haven -> retornar true
        assertEquals(true, twdGameManager.isDoorToSafeHaven(6,6));
    }

    @Test
    public void test11isDoorToSafeHaven() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste verificar se é safe haven -> retornar false
        assertEquals(false, twdGameManager.isDoorToSafeHaven(6,5));
    }
}


// sugestoes de teste

/*

- teste se o zombie for para a posicao de um humano q n tenha equipamento -> deve matar o humano
- testar cada movimento de humano e zombie se for maior que o alcance, deve dar false
- testar se um zombie se tentar mover para uma posicao onde esteja um cão, deve dar false
- Testar se um zombie vai para cima de um equipamento - deve dar true



- testar outras funções
- testar classes - nao testar getters

 */
