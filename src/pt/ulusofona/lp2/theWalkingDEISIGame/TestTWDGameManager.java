package pt.ulusofona.lp2.theWalkingDEISIGame;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestTWDGameManager {

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
        equipamento.setSalvacoes();

        assertEquals(-5, equipamento.getId());
        assertEquals(0, equipamento.getIdTipo());
        assertEquals(3, equipamento.getX());
        assertEquals(2, equipamento.getY());
        assertEquals("equipment_0.png", equipamento.getImagePNG());
        assertEquals("Escudo de Madeira", equipamento.getTitulo());
        assertEquals("Escudo de Madeira", equipamento.getDescricao());
        assertEquals(1, equipamento.getSalvacoes());
        equipamento.setDuracao(1);

        assertEquals(0, equipamento.getDuracao());

        //IdTipo = 1
        Equipamento equipamento0 = new Equipamento(-20,1,0,0);

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

        // IdTipo = 9
        Equipamento equipamento9 = new Equipamento(-9,9,3,2);

        // IdTipo = 10
        Equipamento equipamento8 = new Equipamento(-7, 10, 3, 1);
    }


    @Test
    public void test18CreateNewCreature() {
        // testar a criação de uma nova criatura e mudança de posição

        Creature criatura = new Zombie(10, 2, "Dragon", 4, 6);
        assertEquals(4, criatura.getX());
        assertEquals(6, criatura.getY());
        assertEquals("Dragon", criatura.getNome());
        assertEquals("militar-zombie.png", criatura.getImagePNG());
        assertEquals(0, criatura.getEquipamentosApanhados());
        assertEquals(0, criatura.getIdTipoEquipamento());
        assertEquals("jogo", criatura.getLocal());

        Creature criatura1 = new Zombie(10, 3, "Dragon", 4, 6);
        Creature criatura2 = new Humano(10, 7, "Dragon", 4, 6);
        criatura2.setEquipa(20);
        criatura2.setNomeEquipa("Os Outros");
        criatura2.setImagePNG("militar-zombie.png");
        criatura2.setLocal("morta");
        criatura2.getX();
        criatura2.getY();
        criatura2.getNome();
        criatura2.getImagePNG();
        criatura2.getEquipamentosApanhados();
        criatura2.getIdTipoEquipamento();
        criatura2.getLocal();
        criatura2.colocaAZeroEquipamentos();
        criatura2.humanoParaZombie();
        criatura2.setEnvenenado(true);
        criatura2.getEnvenenado();
        criatura2.toString();
        criatura2.setLocal("safe haven");
        criatura2.toString();
        criatura2.setLocal("viva");
        criatura2.toString();
        criatura1.getMoverDiagonal();
        criatura1.setX(4);
        criatura1.setY(4);
        criatura1.getIdTipo();
        criatura1.adicionaEquipamentosEncontrados(1);
        criatura1.setEquipmentId(0);
        criatura1.getIdEquipamento();
        criatura1.setIdTipoEquipamento(0);
        criatura1.colocaAZeroEquipamentos();
        criatura1.humanoParaZombie();
        criatura1.getAlcance();
        criatura1.setLocal("morta");
        criatura1.setEquipa(10);
        criatura1.setNomeEquipa("Os Outros");
        criatura1.setImagePNG("idoso-zombie.png");
        criatura1.setEnvenenado(true);
        criatura1.getEnvenenado();
        criatura1.toString();
        criatura1.setLocal("safe haven");
        criatura1.toString();
        criatura1.setLocal("viva");
        criatura1.toString();
        criatura1.setMortos();
        criatura1.getMortos();
        criatura2.setMortos();
        criatura2.getMortos();
        criatura2.getAlcance();
        criatura2.setIdTipoEquipamento(5);
        criatura2.getIdEquipamento();
        criatura2.setEquipmentId(-1);
        criatura2.adicionaEquipamentosEncontrados(1);
        criatura2.setX(3);
        criatura2.setY(2);
        criatura2.getId();
        criatura2.getEquipa();
        criatura2.getIdTipo();
        criatura2.getMoverDiagonal();

        Creature criatura3 = new Humano(2,5,"zeus", 3,3);
        Creature criatura4 = new Humano(3,6,"darwid", 3,3);
        Creature criatura5 = new Humano(4,8,"richie",2,1);
        Creature criatura6 = new Humano(5,9,"davi",1,2);
        criatura2.humanoParaZombie();
        criatura3.humanoParaZombie();
        criatura4.humanoParaZombie();
        criatura5.humanoParaZombie();
        criatura6.humanoParaZombie();

        Creature zombie1 = new Zombie(2,0,"zombiw1",1,1);
        Creature zombie2 = new Zombie(3,1,"zombie2",1,2);
        Creature zombie3 = new Zombie(4,2,"ZOMBIE3",1,3);
        Creature zombie4 = new Zombie(4,4,"oi",2,2);
        zombie4.getId();
        zombie4.getEquipa();

    }


    @Test
    public void test19move() {
        InvalidTWDInitialFileException invalidTWDInitialFileException = new InvalidTWDInitialFileException(5,true,"");
        assertEquals(true,invalidTWDInitialFileException.validNrOfCreatures());
        assertEquals(true,invalidTWDInitialFileException.validCreatureDefinition());

        InvalidTWDInitialFileException invalidTWDInitialFileException1 = new InvalidTWDInitialFileException(1,false,"linha");
        assertEquals(false,invalidTWDInitialFileException1.validNrOfCreatures());
        assertEquals(false,invalidTWDInitialFileException1.validCreatureDefinition());
        assertEquals("linha",invalidTWDInitialFileException1.getErroneousLine());
    }

    @Test
    public void test20move() throws IOException, InvalidTWDInitialFileException {
        String string = new String();
        string = "7 7\n" +"10\n" +  "10\n" + "1 : 1 : Freddie M. : 4 : 3\n" + "2 : 7 : Jackie Chan : 3 : 5\n" + "3 : 6 : Alice : 5 : 5\n" +
                "4 : 8 : Ash : 2 : 2\n" + "5 : 5 : Sam : 1 : 1\n" + "10 : 9 : Cão : 0 : 3  \n" + "6 : 2 : Paciente Zero : 4 : 4\n" + "7 : 3 : Paciente Zeros : 5 : 4\n" + "8 : 4 : Paciente Zerosa : 4 : 2\n" + "9 : 0 : Paciente Um : 2 : 1\n" +
                "4\n" + "-1 : 9 : 1 : 0\n" + "-2 : 0 : 2 : 3\n" + "-3 : 8 : 2 : 0\n" + "-4 : 2 : 2 : 1\n" + "2\n" + "6 : 6\n" + "0 : 0";
        File file = new File("dadosTeste");
        FileWriter fileWriter = new FileWriter(file.getPath());
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(string);
        printWriter.close();

        TWDGameManager twdGameManager = new TWDGameManager();
        twdGameManager.getWorldSize();
        twdGameManager.getInitialTeam();
        twdGameManager.getAuthors();
        twdGameManager.getCreatures();
        twdGameManager.getHumans();
        twdGameManager.getZombies();
        twdGameManager.getEquipamentos();
        twdGameManager.getCurrentTeamId();
        List<Creature> criaturas = new ArrayList<>();
        List<Equipamento> equipamentos = new ArrayList<>();
        List<Porta> portas = new ArrayList<>();
        criaturas.add(new Zombie(1, 0, "Freddy M.", 3, 3));
        criaturas.add(new Humano(2, 8, "Jackie Chan", 3, 4));
        criaturas.add(new Humano(3, 6, "Alice", 5, 5));
        criaturas.add(new Humano(4, 6, "Ash", 2, 2));
        criaturas.add(new Humano(5, 9, "Sam", 1, 1));
        criaturas.add(new Humano(6, 5, "Paciente Zero", 4, 4));
        criaturas.add(new Zombie(7, 1, "Paciente Zeros", 5, 4));
        criaturas.add(new Zombie(8, 4, "Paciente Zerosa", 4, 3));
        criaturas.add(new Zombie(8,4,"zombie",2,4));
        equipamentos.add(new Equipamento(-1, 9, 1, 2));
        equipamentos.add(new Equipamento(-2, 2, 3, 2));
        portas.add(new Porta(0,0));
        portas.add(new Porta(2,4));
        twdGameManager.isDay();
        twdGameManager.move(2, 2, 1, 2);
        twdGameManager.getElementId(1,2);
        twdGameManager.getElementId(0,0);
        twdGameManager.getGameResults();
        twdGameManager.getEquipmentId(1);
        twdGameManager.getEquipmentId(-30);
        twdGameManager.getEquipmentTypeId(-1);
        twdGameManager.getEquipmentTypeId(-30);
        twdGameManager.popCultureExtravaganza();
        twdGameManager.getEquipmentInfo(-1);
        twdGameManager.verificarSobrePosicao(3,3,3,5);
        twdGameManager.move(3,4,3,5);
        twdGameManager.verificarSobrePosicao(3,3,4,2);
        twdGameManager.verificarSobrePosicao(1,1,1,3);
        twdGameManager.verificarSobrePosicao(5,3,4,4);
        twdGameManager.verificarSobrePosicao(4,6,4,4);
        twdGameManager.verificarSobrePosicao(3,5,3,5);
        twdGameManager.verificarSobrePosicao(5,3,5,3);
        twdGameManager.verificarSobrePosicao(3,1,3,5);
        twdGameManager.verificarSobrePosicao(3,1,3,5);
        twdGameManager.verificarSobrePosicao(2,0,2,2);
        twdGameManager.verificarSobrePosicao(2,2,5,3);
        twdGameManager.verificarSobrePosicao(2,4,4,2);
        twdGameManager.getElementId(1,2);
        twdGameManager.getIdsInSafeHaven();
        twdGameManager.move(0,0,-1,-1);
        twdGameManager.move(2,2,1,2);
        twdGameManager.move(1,2,3,2);
        twdGameManager.move(4,3,3,3);
        twdGameManager.move(3,4,3,5);
        twdGameManager.getGameStatistics();
        twdGameManager.isDoorToSafeHaven(0,0);

    }

}
