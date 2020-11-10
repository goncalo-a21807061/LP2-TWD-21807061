package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TWDGameManager {

    static List<Humano> humanos = new ArrayList<>();
    static List<Zombie> zombies = new ArrayList<>();
    static List<Equipamento> equipamentos = new ArrayList<>();

    int[][] tabuleiro;
    int width;
    int height;
    int rows;
    int columns;

    int equipaInicial;
    int currentTeam;
    int turnos = -1;
    int nrCriaturas;
    int nrEquipamentos;


    public TWDGameManager() {}

    public boolean startGame(File ficheiroInicial) {
        String linha;
        int count = 0;
        //ler o ficheiro passado por argumento
        try {
            Scanner leitorFicheiro = new Scanner(ficheiroInicial);
            while(leitorFicheiro.hasNextLine()) {
                linha = leitorFicheiro.nextLine();
                if(count == 0) {
                    String dados[] = linha.split(" ");
                    rows = Integer.parseInt(dados[0]);
                    columns = Integer.parseInt(dados[1]);
                } else if(count == 1) {
                    String dados[] = linha.split(" : ");
                    equipaInicial = Integer.parseInt(dados[0]);
                } else if(count == 2) {
                    String dados[] = linha.split(" : ");
                    nrCriaturas = Integer.parseInt(dados[0]);
                } else if(count > 2 && count <= count + nrCriaturas) {
                    String dados[] = linha.split(" : ");
                    int id = Integer.parseInt(dados[0].replace(" :",""));
                    int idTipo = Integer.parseInt(dados[1].replace(": ",""));
                    String nome = dados[2];
                    int x = Integer.parseInt(dados[3]);
                    int y = Integer.parseInt(dados[4]);
                    if(idTipo == 1) {
                        Humano humano = new Humano(id,idTipo,nome,x,y);
                        humanos.add(humano);
                    } else if(idTipo == 1) {
                        Zombie zombie = new Zombie(id, idTipo, nome, x, y);
                        zombies.add(zombie);
                    }
                } else if(count > count + nrCriaturas) {
                    String dados[] = linha.split(" : ");
                    nrEquipamentos = Integer.parseInt(dados[1]);
                } else if(count > count + nrCriaturas + 1 && count <= count + nrCriaturas +nrEquipamentos) {
                    String dados[] = linha.split(" : ");
                    int id = Integer.parseInt(dados[0].replace(" ",""));
                    int idTipo = Integer.parseInt(dados[1].replace(": ",""));
                    int x = Integer.parseInt(dados[2]);
                    int y = Integer.parseInt(dados[3]);
                    Equipamento equipamento = new Equipamento(id,idTipo,x,y);
                    equipamentos.add(equipamento);
                }
                count++;
            }
            leitorFicheiro.close();
        } catch (FileNotFoundException exception) {
            System.out.println("Erro. Ficheiro não encontrado");
        }

        //this.width = rows - 1;    ????
        //this.height = columns - 1; ????
        return true;
    }


    public int[] getWorldSize() {
        int[] worldSize = new int[2];
        worldSize[0] = rows;
        worldSize[1] = columns;
        return worldSize;
    }

    public int getInitialTeam() {
        return equipaInicial;
    }

    public List<Humano> getHumans() {
        return humanos;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public boolean move(int xO, int yO, int xD, int yD) {
        if(!gameIsOver()){
            turnos++;

        }
        return true;
    }

    public boolean gameIsOver() {
        if (turnos >= 6) {
            return true;
        }
        return false;
    }

    public List<String> getAuthors() {
        List<String> authors = new ArrayList<>();
        authors.add("Gonçalo Monteiro");
        return authors;
    }

    public int getCurrentTeamId() {
        return currentTeam;
    }

    public int getElementId(int x, int y) {
        return tabuleiro[x][y];
    }

    public List<String> getSurvivors() {
        List<String> survivors = new ArrayList<>();
        survivors.add("Nr. de turnos terminados:\n");
        survivors.add(String.valueOf(turnos)+"\n\n");
        survivors.add("OS VIVOS:\n");
        for(Humano humano: humanos) {
            survivors.add(humano.getId() + " " + humano.getNome());
        }
        for(Zombie zombie: zombies) {
            survivors.add(String.valueOf(zombie.getId()));
        }
        return survivors;

        // FALTA ORDENAR OS ID´S ANTES DE ADICIONAR
    }

    public boolean isDay() {
        if(turnos % 2 == 0) {
            return true;
        }
        return false;
    }

    public boolean hasEquipment(int creatureId, int equipmentTypeId) {
        for(Humano humano: humanos) {
            if(humano.getId() == creatureId) { //&& //humano.    -> tem em sua posse um equipamento com aquele id) {

            }
        }
        return true;
    }


}

