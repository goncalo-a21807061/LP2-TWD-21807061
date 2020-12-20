package pt.ulusofona.lp2.theWalkingDEISIGame;

import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestTWDGameManager {

    @Test
    public void test01Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento humano - andar 2 posicoes
        assertEquals(true, twdGameManager.move(3, 3, 3, 1));

    }


    @Test
    public void test02Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento fora do grafico humano
        assertEquals(true, twdGameManager.move(3, 3, 3, -1));

    }

    @Test
    public void test03Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste humano ataca Zombie
        assertEquals(true, twdGameManager.move(3, 4, 3, 3));
    }

    @Test
    public void test04Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste movimento fora do grafico zombie
        assertEquals(true, twdGameManager.move(4, 4, 4, -1));

    }

    @Test
    public void test05Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste movimento humano safe haven
        assertEquals(true, twdGameManager.move(5, 5, 6, 6));
    }

    @Test
    public void test06Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste movimento zombie safe haven
        assertEquals(true, twdGameManager.move(5, 4, 6, 6));
    }

    @Test
    public void test07Move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste movimento humano apanhar a espada
        assertEquals(true, twdGameManager.move(2, 2, 2, 3));
    }

    @Test
    public void test08GetInitialTeam() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        //teste equipa inicial
        assertEquals(10, twdGameManager.getInitialTeam());
    }

    @Test
    public void test10isDoorToSafeHaven() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste verificar se é safe haven -> retornar true
        assertEquals(true, twdGameManager.isDoorToSafeHaven(6, 6));
    }


    @Test
    public void test11isDoorToSafeHaven() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));

        // teste verificar se é safe haven -> retornar false
        assertEquals(false, twdGameManager.isDoorToSafeHaven(6, 5));
    }

    @Test
    public void test12getIdsInSafeHaven() {
        TWDGameManager twdGameManager12 = new TWDGameManager();
        twdGameManager12.startGame(new File("dados.txt"));
        List<Integer> idsinSafeHaven = new ArrayList<>();

        // teste verificar se é safe haven -> retornar false
        assertEquals(idsinSafeHaven, twdGameManager12.getIdsInSafeHaven());

        twdGameManager12.move(5, 5, 6, 6);
        idsinSafeHaven.add(3);

        //teste verificar ids in safe haven -> no inicio ainda não há nenhum
        assertEquals(idsinSafeHaven, twdGameManager12.getIdsInSafeHaven());
    }

    @Test
    public void test13getCurrentTeamId() {
        TWDGameManager twdGameManager13 = new TWDGameManager();
        twdGameManager13.startGame(new File("dados.txt"));
        int currentTeam = 10;

        assertEquals(currentTeam, twdGameManager13.getCurrentTeamId());
    }

    @Test
    public void test14LoadGame() {
        TWDGameManager twdGameManager14 = new TWDGameManager();

        //teste load game - deve retornar false
        assertEquals(false, twdGameManager14.loadGame(new File("dados.txt")));
    }

    @Test
    public void test15LoadGame() {
        TWDGameManager twdGameManager15 = new TWDGameManager();

        //teste load game - deve retornar false
        assertEquals(false, twdGameManager15.loadGame(new File("ola")));
    }

    @Test
    public void test16CreateNewDoor() {
        // testar a criação de uma porta e a mudança de posiçao

        Porta porta = new Porta(1, 2);

        assertEquals(1, porta.getX());
        assertEquals(2, porta.getY());

        porta.setX(2);
        porta.setY(3);

        assertEquals(2, porta.getX());
        assertEquals(3, porta.getY());
        assertEquals("door.png", porta.getImagePNG());
    }

    @Test
    public void test17CreateNewEquipment() {
        //testar a criação de equipamentos com os vários IDTipos e a mudança de posiçao
        Equipamento equipamento = new Equipamento(-5, 0, 2, 1);
        assertEquals(2, equipamento.getX());
        assertEquals(1, equipamento.getY());

        equipamento.setX(3);
        equipamento.setY(2);

        assertEquals(3, equipamento.getX());
        assertEquals(2, equipamento.getY());
        assertEquals("equipment_0.png", equipamento.getImagePNG());
        assertEquals("Escudo de Madeira", equipamento.getTitulo());

        equipamento.setDuracao(1);

        assertEquals(0, equipamento.getDuracao());


        //IdTipo = 2
        Equipamento equipamento1 = new Equipamento(-6, 2, 3, 1);

        // IdTipo = 3
        Equipamento equipamento2 = new Equipamento(-7, 3, 3, 1);

        // IdTipo = 4
        Equipamento equipamento3 = new Equipamento(-7, 4, 3, 1);

        // IdTipo = 5
        Equipamento equipamento4 = new Equipamento(-7, 5, 3, 1);

        // IdTipo = 6
        Equipamento equipamento5 = new Equipamento(-7, 6, 3, 1);

        // IdTipo = 7
        Equipamento equipamento6 = new Equipamento(-7, 7, 3, 1);

        // IdTipo = 8
        Equipamento equipamento7 = new Equipamento(-7, 8, 3, 1);

        // IdTipo = 10
        Equipamento equipamento8 = new Equipamento(-7, 10, 3, 1);
    }

}
/*

    @Test
    public void test18CreateNewCreature() {
        // testar a criação de uma nova criatura e mudança de posição

        Creature criatura = new Creature(10,2,"Dragon",4,6);
        assertEquals(4,criatura.getX());
        assertEquals(6,criatura.getY());
        assertEquals("Dragon", criatura.getNome());
        assertEquals("zombie.png",criatura.getImagePNG());
        assertEquals(0, criatura.getEquipamentosApanhados());
        assertEquals(0, criatura.getIdTipoEquipamento());
        assertEquals("jogo", criatura.getLocal());
        // falta tostring

        Creature criatura1 = new Creature(10,3,"Dragon",4,6);
        Creature criatura2 = new Creature(10,7,"Dragon",4,6);
    }

    @Test
    public void test18move() {
        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.startGame(new File("dados.txt"));
        List<Creature> criaturas = new ArrayList<>();
        List<Equipamento> equipamentos = new ArrayList<>();
        criaturas.add(new Creature(1,0,"Freddy M.",3,3));
        criaturas.add(new Creature(2,8,"Jackie Chan",3,4));
        criaturas.add(new Creature(3,6,"Alice",5,5));
        criaturas.add(new Creature(4,6,"Ash",2,2));
        criaturas.add(new Creature(5,9,"Sam",1,1));
        criaturas.add(new Creature(6,5,"Paciente Zero",4,4));
        criaturas.add(new Creature(7,1,"Paciente Zeros",5,4));
        criaturas.add(new Creature(8,4,"Paciente Zerosa",4,2));
        equipamentos.add(new Equipamento(-1,9,1,2));
        assertEquals(true,twdGameManager.move(2,2,1,2));
        assertEquals(20, twdGameManager.getCurrentTeamId());
        assertEquals(4,twdGameManager.getElementId(1,2));
        assertEquals(0,twdGameManager.getElementId(0,0));
        assertEquals(-2,twdGameManager.getElementId(2,3));
        assertEquals(false, twdGameManager.gameIsOver());
        assertEquals(true,twdGameManager.move(3,3,2,3));
        assertEquals(10, twdGameManager.getCurrentTeamId());

        assertEquals(false,twdGameManager.move(1,2,1,1));
        assertEquals(true,twdGameManager.move(1,1,3,1));
        assertEquals(20, twdGameManager.getCurrentTeamId());
        assertEquals(true,twdGameManager.move(5,4,4,4));
        assertEquals(true, twdGameManager.move(3,1,4,2));

        assertEquals(false,twdGameManager.move(2,3,2,0));
        assertEquals(true, twdGameManager.move(2,3,3,4));

        assertEquals(true, twdGameManager.move(5,5,6,6));

        assertEquals(true, twdGameManager.move(5,4,5,3));
        assertEquals(true, twdGameManager.move(1,2,2,3));
        assertEquals(false, twdGameManager.move(4,2,4,1));
        assertEquals(true, twdGameManager.move(2,3,2,2));
        assertEquals(true, twdGameManager.move(1,2,1,0));
        assertEquals(true, twdGameManager.move(4,2,4,0));
        assertEquals(true, twdGameManager.gameIsOver());
    }




}

 */



