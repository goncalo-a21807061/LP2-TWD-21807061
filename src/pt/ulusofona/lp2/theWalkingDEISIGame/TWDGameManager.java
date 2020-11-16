package pt.ulusofona.lp2.theWalkingDEISIGame;

import javax.management.openmbean.TabularData;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TWDGameManager {

    static List<Humano> humanos;
    static List<Zombie> zombies;
    static List<Equipamento> equipamentos;

    int[][] tabuleiro;
    int width;
    int height;
    int rows;
    int columns;

    int equipaInicial;
    int currentTeam;
    int turnos = 0;
    int nrCriaturas;
    int nrEquipamentos;


    public TWDGameManager() {}

    public boolean startGame(File ficheiroInicial) {
        BufferedReader leitorFicheiro = null;
        String linha;
        humanos = new ArrayList<>();
        zombies = new ArrayList<>();
        equipamentos = new ArrayList<>();
        int count = 0,count1 = 0;
        int id,idTipo,x,y;
        try {
            leitorFicheiro = new BufferedReader(new FileReader(ficheiroInicial.getPath()));
            while((linha = leitorFicheiro.readLine()) != null) {
                if(count == 0) {
                    String dados[] = linha.split(" ");
                    rows = Integer.parseInt(dados[0].trim());
                    columns = Integer.parseInt(dados[1].trim());
                    this.width = rows - 1;
                    this.height = columns - 1;
                    tabuleiro = new int [rows][columns];
                } else if(count == 1) {
                    equipaInicial = Integer.parseInt(linha.trim());
                } else if(count == 2) {
                    nrCriaturas = Integer.parseInt(linha.trim());
                    count1 = count + nrCriaturas;
                } else if(count > 2 && count <= count1) {
                    String dados[] = linha.split(":");
                    if(dados.length > 4) {
                        id = Integer.parseInt(dados[0].trim());
                        idTipo = Integer.parseInt(dados[1].trim());
                        String nome = dados[2].trim();
                        x = Integer.parseInt(dados[3].trim());
                        y = Integer.parseInt(dados[4].trim());
                        if (idTipo == 1) {
                            Humano humano = new Humano(id, idTipo, nome, x, y);
                            humanos.add(humano);
                        } else if (idTipo == 0) {
                            Zombie zombie = new Zombie(id, idTipo, nome, x, y);
                            zombies.add(zombie);
                        }
                        tabuleiro[y][x] = id;
                    }
                } else if(count > count1 && count <= count1 + 1) {
                    nrEquipamentos = Integer.parseInt(linha.trim());
                } else if(count > count1+1 && count <= count1 + 1 + nrEquipamentos) {
                    String dados[] = linha.split(":");
                    if(dados.length > 3) {
                        id = Integer.parseInt(dados[0].trim());
                        idTipo = Integer.parseInt(dados[1].trim());
                        x = Integer.parseInt(dados[2].trim());
                        y = Integer.parseInt(dados[3].trim());
                        Equipamento equipamento = new Equipamento(id, idTipo, x, y);
                        equipamentos.add(equipamento);
                        tabuleiro[y][x] = id;
                    }
                }
                count++;
            }
            leitorFicheiro.close();
            return true;
        } catch (IOException e) {
            return false;
        }
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
        int id,idEquipamento;
        if(!gameIsOver()){
            //se for turno dos humanos nao pode deixar mover zombies e vice-versa
            if(currentTeam == 0) {
                if(!(xO-xD > 1 || yO-yD > 1)) {
                    for (Humano humano : humanos) {
                        for(Equipamento equipamento: equipamentos) {
                            idEquipamento = equipamento.getIdTipo();
                            id = humano.getId();
                            if (tabuleiro[yO][xO] == id) {
                                if (tabuleiro[yD][xD] == 0) {
                                    humano.setX(xD);
                                    humano.setY(yD);
                                    tabuleiro[yD][xD] = id;
                                    tabuleiro[yO][xO] = 0;
                                    turnos++;
                                    if (currentTeam == 1) {
                                        currentTeam = 0;

                                    } else {
                                        currentTeam = 1;
                                    }
                                    return true;
                                }
                                if(tabuleiro[yD][xD] == idEquipamento) {
                                    humano.setX(xD);
                                    humano.setY(yD);
                                    humano.adicionaEquipamentosEncontrados(1);
                                    humano.setEquipmentId(idEquipamento);
                                }
                            }
                        }
                    }
                }
                return false;
            } else {
                if(!(xO-xD > 1 || yO-yD > 1)) {
                    for (Zombie zombie : zombies) {
                        for (Equipamento equipamento : equipamentos) {
                            idEquipamento = equipamento.getId();
                            id = zombie.getId();
                            if (tabuleiro[yO][xO] == id) {
                                if (tabuleiro[yD][xD] == 0) {
                                    zombie.setX(xD);
                                    zombie.setY(yD);
                                    tabuleiro[yD][xD] = id;
                                    tabuleiro[yO][xO] = 0;
                                    turnos++;
                                    if (currentTeam == 1) {
                                        currentTeam = 0;
                                    } else {
                                        currentTeam = 1;
                                    }
                                    return true;
                                }
                                if(tabuleiro[yD][xD] == idEquipamento) {
                                    zombie.setX(xD);
                                    zombie.setY(yD);
                                    zombie.adicionaEquipamentosDestruidos(1);
                                    equipamentos.remove(equipamento);
                                }
                            }
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }

    public boolean gameIsOver() {
        if (turnos >= 12) {
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
        return tabuleiro[y][x];
    }

    public List<String> getSurvivors() {
        humanos.sort(Comparator.comparing(Humano::getId)); //ordenar ID's
        zombies.sort(Comparator.comparing(Zombie::getId));
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
    }

    public boolean isDay() {
        if(turnos == 2 || turnos == 3 || turnos == 6 || turnos == 7 || turnos == 10 || turnos == 11) {
            return false;
        }
        return true;
    }


    public boolean hasEquipment(int creatureId, int equipmentTypeId) {
        for(Humano humano: humanos) {
            if(humano.getId() == creatureId && humano.getIdEquipamento() == equipmentTypeId) {
                return true;
            }
        }
        return false;
    }
}
