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

        //ver se Humanos e Zombies estao na posicao certa
        assertEquals(1,twdGameManager.getElementId(3,3));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,4));
        ;
        //mover Humano da posição (3,3) para a posicao (3,2)
        twdGameManager.move(3,3,3,2);
        //Verificar se Humano se moveu
        assertEquals(1,twdGameManager.getElementId(3,2));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,4));
    }


    @Test
    public void test02Move() {
        //teste movimento fora do grafico humano
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //ver se Humanos e Zombies estao na posicao certa
        assertEquals(1,twdGameManager.getElementId(3,3));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,4));
        ;
        //mover Humano da posição (3,3) para a posicao (3,-1) -> Fora do ecra
        twdGameManager.move(3,3,3,-1);
        //Verificar se Humano se moveu
        assertEquals(1,twdGameManager.getElementId(3,3));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,4));
    }

    @Test
    public void test03Move() {
        //teste movimento normal zombie
        //teste movimento normal humano
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //ver se Humanos e Zombies estao na posicao certa
        assertEquals(1,twdGameManager.getElementId(3,3));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,4));
        ;
        //mover Zombie da posição (4,4) para a posicao (4,3)
        twdGameManager.move(4,4,4,3);
        //Verificar se Humano se moveu
        assertEquals(1,twdGameManager.getElementId(3,3));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,3));
    }

    @Test
    public void test04Move() {
        //teste movimento fora do grafico zombie
        //teste movimento normal humano
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //ver se Humanos e Zombies estao na posicao certa
        assertEquals(1,twdGameManager.getElementId(3,3));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,4));
        ;
        //mover Humano da posição (3,3) para a posicao (3,2)
        twdGameManager.move(4,4,4,-1);
        //Verificar se Humano se moveu
        assertEquals(1,twdGameManager.getElementId(3,3));
        assertEquals(2,twdGameManager.getElementId(3,4));
        assertEquals(3,twdGameManager.getElementId(4,4));
    }


    //se der para implementar o Equipamento testar se o humano apanha o equipamento ou nao etc
}
