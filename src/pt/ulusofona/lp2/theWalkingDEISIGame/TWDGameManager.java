package pt.ulusofona.lp2.theWalkingDEISIGame;

import javax.sound.sampled.Port;
import java.io.*;
import java.util.*;

public class TWDGameManager {

    static List<Creature> criaturas = new ArrayList<>();
    static List<Equipamento> equipamentos = new ArrayList<>();
    static List<Porta> portas = new ArrayList<>();
    static List<Creature> safeHeaven = new ArrayList<>();
    static List<Creature> envenenados = new ArrayList<>();

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
    int nrPortas;
    int idTipoEquipamento;
    int bala;
    int turnosVeneno = 0;
    int idTipo;
    Boolean moveDiagonal;


    public TWDGameManager() {
    }

    public boolean startGame(File ficheiroInicial) {
        BufferedReader leitorFicheiro = null;
        String linha;
        int count = 0, count1 = 0, count2 = 0;
        int id, idTipo, x, y;
        try {
            leitorFicheiro = new BufferedReader(new FileReader(ficheiroInicial.getPath()));
            while ((linha = leitorFicheiro.readLine()) != null) {
                if (count == 0) {
                    String dados[] = linha.split(" ");
                    rows = Integer.parseInt(dados[0].trim());
                    columns = Integer.parseInt(dados[1].trim());
                    this.width = rows - 1;
                    this.height = columns - 1;
                    tabuleiro = new int[rows][columns];
                } else if (count == 1) {
                    equipaInicial = Integer.parseInt(linha.trim());
                    currentTeam = equipaInicial;
                } else if (count == 2) {
                    nrCriaturas = Integer.parseInt(linha.trim());
                    count1 = count + nrCriaturas;
                } else if (count > 2 && count <= count1) {
                    String dados[] = linha.split(":");
                    if (dados.length > 4) {
                        id = Integer.parseInt(dados[0].trim());
                        idTipo = Integer.parseInt(dados[1].trim());
                        String nome = dados[2].trim();
                        x = Integer.parseInt(dados[3].trim());
                        y = Integer.parseInt(dados[4].trim());
                        Creature criatura = new Creature(id, idTipo, nome, x, y);
                        criaturas.add(criatura);
                        tabuleiro[y][x] = id;
                    }
                } else if (count > count1 && count <= count1 + 1) {
                    nrEquipamentos = Integer.parseInt(linha.trim());
                    count2 = count1 + nrEquipamentos;
                } else if (count > count1 + 1 && count <= count1 + 1 + nrEquipamentos) {
                    String dados[] = linha.split(":");
                    if (dados.length > 3) {
                        id = Integer.parseInt(dados[0].trim());
                        idTipo = Integer.parseInt(dados[1].trim());
                        x = Integer.parseInt(dados[2].trim());
                        y = Integer.parseInt(dados[3].trim());
                        Equipamento equipamento = new Equipamento(id, idTipo, x, y);
                        equipamentos.add(equipamento);
                        tabuleiro[y][x] = id;
                    }
                } else if (count > count2 +1 && count <= count2 + 2) {
                    nrPortas = Integer.parseInt(linha.trim());
                } else if (count > count2 + 2 && count <= count2 + 2 + nrPortas) {
                    String dados[] = linha.split(":");
                    if (dados.length > 0) {
                        x = Integer.parseInt(dados[0].trim());
                        y = Integer.parseInt(dados[1].trim());
                        Porta porta = new Porta(x, y);
                        portas.add(porta);
                        tabuleiro[y][x] = 99;
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


    public boolean move(int xO, int yO, int xD, int yD) {
        int id, idEquipamento,idHumano,idZombie;
        if (!gameIsOver()) {
            //se for turno dos humanos nao pode deixar mover zombies e vice-versa
            if (currentTeam == 10) {
                for (Creature humano : criaturas) {
                    if (humano.getEquipa() == 10) {
                        for (Equipamento equipamento : equipamentos) {
                            for (Creature zombie : criaturas) {
                                moveDiagonal = humano.getMoverDiagonal();
                                idZombie = zombie.getId();
                                idEquipamento = equipamento.getId();
                                idTipo = humano.getIdTipo();
                                idTipoEquipamento = equipamento.getIdTipo();
                                id = humano.getId();
                                if (tabuleiro[yO][xO] == id) {
                                    // validar se é Idoso Humano -> Se for só pode jogar nos turnos diurnos
                                    if(idTipo == 8 && (turnos == 0 || turnos == 4 || turnos == 8)) {
                                        if ((Math.abs(xO - xD) <= humano.getAlcance() && Math.abs(yO - yD) <= humano.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                //FALTA -> verificar se não é um idoso vivo, se não for, pode transportar equipamento
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setIdTipoEquipamento(idTipoEquipamento);
                                                humano.adicionaEquipamentosEncontrados(1);
                                                humano.setEquipmentId(idEquipamento);
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == 99) {
                                                tabuleiro[yO][xO] = 0;
                                                safeHeaven.add(humano);
                                                humano.setLocal("safe haven");  // toString
                                                //criaturas.remove(humano);
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idZombie) {
                                                if (zombie.getEquipa() == 20) {
                                                    if (humano.getIdEquipamento() == 0) {
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        //criaturas.remove(humano);
                                                        humano.setLocal("morta");   // toString
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                }
                                                if (humano.getIdTipoEquipamento() == 0) {
                                                    humano.setEquipmentId(0);
                                                    equipamentos.remove(equipamento);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 1) {
                                                    tabuleiro[yD][xD] = 0;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
                                                    //criaturas.remove(zombie);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 2) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (bala == 0) {
                                                                equipamentos.remove(equipamento1);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                tabuleiro[yD][xD] = 0;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
                                                                //criaturas.remove(zombie);
                                                                equipamento1.setDuracao(1);
                                                            }
                                                        }
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 3) {
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 4) {
                                                    if (zombie.getIdTipo() != 3) {// Idoso zombie
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                        // criaturas.remove(humano);
                                                        equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 5) {
                                                    if (zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                        //criaturas.remove(humano);
                                                        equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 6) {
                                                    tabuleiro[yD][xD] = 0;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
                                                    //criaturas.remove(zombie);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 7) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (bala == 0) {
                                                                equipamentos.remove(equipamento1);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                tabuleiro[yO][xO] = 0;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
                                                                //  criaturas.remove(zombie);
                                                                equipamento1.setDuracao(1);
                                                            }
                                                        }
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 8) {
                                                    idEquipamento = 8;
                                                    // mal implementado -> humano nao morre
                                                    if (turnosVeneno > 2) {
                                                        //  criaturas.remove(humano);
                                                    }
                                                    turnos++;
                                                    turnosVeneno++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 9) {
                                                    if (idEquipamento == 8) {
                                                        humano.setEquipmentId(0);
                                                        tabuleiro[yD][xD] = id;
                                                        equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 10) {
                                                    humano.setEquipmentId(0);
                                                    equipamentos.remove(equipamento);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                }
                                            }
                                        }
                                    } else if (idTipo != 8){
                                        if ((Math.abs(xO - xD) <= humano.getAlcance() && Math.abs(yO - yD) <= humano.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                //FALTA -> verificar se não é um idoso vivo, se não for, pode transportar equipamento
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setIdTipoEquipamento(idTipoEquipamento);
                                                humano.adicionaEquipamentosEncontrados(1);
                                                humano.setEquipmentId(idEquipamento);
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == 99) {
                                                tabuleiro[yO][xO] = 0;
                                                safeHeaven.add(humano);
                                                humano.setLocal("safe haven");  // toString
                                                //criaturas.remove(humano);
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idZombie) {
                                                if (zombie.getEquipa() == 20) {
                                                    if (humano.getIdEquipamento() == 0) {
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        //criaturas.remove(humano);
                                                        humano.setLocal("morta");   // toString
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                }
                                                if (humano.getIdTipoEquipamento() == 0) {
                                                    humano.setEquipmentId(0);
                                                    equipamentos.remove(equipamento);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 1) {
                                                    tabuleiro[yD][xD] = 0;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
                                                    //criaturas.remove(zombie);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 2) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (bala == 0) {
                                                                equipamentos.remove(equipamento1);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                tabuleiro[yD][xD] = 0;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
                                                                //criaturas.remove(zombie);
                                                                equipamento1.setDuracao(1);
                                                            }
                                                        }
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 3) {
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 4) {
                                                    if (zombie.getIdTipo() != 3) {// Idoso zombie
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                        // criaturas.remove(humano);
                                                        equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 5) {
                                                    if (zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                        //criaturas.remove(humano);
                                                        equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 6) {
                                                    tabuleiro[yD][xD] = 0;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
                                                    //criaturas.remove(zombie);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 7) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (bala == 0) {
                                                                equipamentos.remove(equipamento1);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                tabuleiro[yO][xO] = 0;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
                                                                //  criaturas.remove(zombie);
                                                                equipamento1.setDuracao(1);
                                                            }
                                                        }
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 8) {
                                                    idEquipamento = 8;
                                                    // mal implementado -> humano nao morre
                                                    if (turnosVeneno > 2) {
                                                        //  criaturas.remove(humano);
                                                    }
                                                    turnos++;
                                                    turnosVeneno++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 9) {
                                                    if (idEquipamento == 8) {
                                                        humano.setEquipmentId(0);
                                                        tabuleiro[yD][xD] = id;
                                                        equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 10) {
                                                    humano.setEquipmentId(0);
                                                    equipamentos.remove(equipamento);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            } else {
                for (Creature zombie : criaturas) {
                    if (zombie.getEquipa() == 20) {
                        for (Equipamento equipamento : equipamentos) {
                            for (Creature humano : criaturas) {
                                idHumano = humano.getId();
                                idTipo = zombie.getIdTipo();
                                idEquipamento = equipamento.getIdTipo();
                                id = zombie.getId();
                                if (tabuleiro[yO][xO] == id) {
                                    // validar se é zombie vampiro, se for só pode jogar nos turnos noturnos
                                    if(idTipo == 4 && (turnos == 3 || turnos == 7 || turnos == 11)) {
                                        if ((Math.abs(xO - xD) <= zombie.getAlcance() && Math.abs(yO - yD) <= zombie.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                zombie.setX(xD);
                                                zombie.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                zombie.setX(xD);
                                                zombie.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                zombie.adicionaEquipamentosEncontrados(1);
                                                equipamentos.remove(equipamento);
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idHumano) {
                                                if (humano.getEquipa() == 10) {
                                                    if (humano.getIdEquipamento() == 0) {
                                                        tabuleiro[yD][xD] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                      //  criaturas.remove(humano);
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                    if (humano.getIdTipoEquipamento() == 0) {
                                                        humano.setEquipmentId(0);
                                                        equipamentos.remove(equipamento);
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 1) {
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(zombie);
                                                        zombie.setLocal("morta");
                                                      //  criaturas.remove(zombie);
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 2) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    equipamentos.remove(equipamento1);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
                                                               //     criaturas.remove(zombie);
                                                                    equipamento1.setDuracao(1);
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 3) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 4) {
                                                        if(zombie.getIdTipo() != 3) {// Idoso zombie
                                                            tabuleiro[yO][xO] = 0;
                                                            tabuleiro[yD][xD] = id;
                                                            envenenados.add(humano);
                                                            humano.setLocal("morta");
                                                          //  criaturas.remove(humano);
                                                            equipamentos.remove(equipamento);
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 5) {
                                                        if(zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                            tabuleiro[yO][xO] = 0;
                                                            tabuleiro[yD][xD] = id;
                                                            envenenados.add(humano);
                                                            humano.setLocal("morta");
                                                          //  criaturas.remove(humano);
                                                            equipamentos.remove(equipamento);
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }  else if (humano.getIdTipoEquipamento() == 7) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    equipamentos.remove(equipamento1);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
                                                               //     criaturas.remove(zombie);
                                                                    equipamento1.setDuracao(1);
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 8) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 9) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 10) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    } else if (idTipo != 4) {
                                        if ((Math.abs(xO - xD) <= zombie.getAlcance() && Math.abs(yO - yD) <= zombie.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                zombie.setX(xD);
                                                zombie.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                zombie.setX(xD);
                                                zombie.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                zombie.adicionaEquipamentosEncontrados(1);
                                                equipamentos.remove(equipamento);
                                                turnos++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idHumano) {
                                                if (humano.getEquipa() == 10) {
                                                    if (humano.getIdEquipamento() == 0) {
                                                        tabuleiro[yD][xD] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                        //  criaturas.remove(humano);
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                    if (humano.getIdTipoEquipamento() == 0) {
                                                        humano.setEquipmentId(0);
                                                        equipamentos.remove(equipamento);
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 1) {
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(zombie);
                                                        zombie.setLocal("morta");
                                                        //  criaturas.remove(zombie);
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 2) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    equipamentos.remove(equipamento1);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
                                                                    //     criaturas.remove(zombie);
                                                                    equipamento1.setDuracao(1);
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 3) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 4) {
                                                        if(zombie.getIdTipo() != 3) {// Idoso zombie
                                                            tabuleiro[yO][xO] = 0;
                                                            tabuleiro[yD][xD] = id;
                                                            envenenados.add(humano);
                                                            humano.setLocal("morta");
                                                            //  criaturas.remove(humano);
                                                            equipamentos.remove(equipamento);
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 5) {
                                                        if(zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                            tabuleiro[yO][xO] = 0;
                                                            tabuleiro[yD][xD] = id;
                                                            envenenados.add(humano);
                                                            humano.setLocal("morta");
                                                            //  criaturas.remove(humano);
                                                            equipamentos.remove(equipamento);
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }  else if (humano.getIdTipoEquipamento() == 7) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    equipamentos.remove(equipamento1);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
                                                                    //     criaturas.remove(zombie);
                                                                    equipamento1.setDuracao(1);
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 8) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 9) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 10) {
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
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
        int count = 0;
        for(Creature criatura : criaturas) {
            if(criatura.getEquipa() == 10) {
                count++;
            }
        }
        if (turnos >= 12 || count == 0) {
            return true;
        }
        return false;
    }

    public List<String> getAuthors() {
        List<String> authors = new ArrayList<>();
        authors.add("Gonçalo Monteiro");
        return authors;
    }

    public List<Creature> getCreatures() {
        return criaturas;
    }

    public int getCurrentTeamId() {
        return currentTeam;
    }

    public int getElementId(int x, int y) {
        for(Creature criatura: criaturas) {
            if(tabuleiro[y][x] == criatura.getId()) {
                return criatura.getId();
            }
        }
        for(Equipamento equipamento: equipamentos) {
            if (tabuleiro[y][x] == equipamento.getId()) {
                return equipamento.getId();
            }
        }
       return 0;
    }

    public List<String> getGameResults() {
        criaturas.sort(Comparator.comparing(Creature::getId)); //ordenar ID's
        safeHeaven.sort(Comparator.comparing(Creature::getId));
        envenenados.sort(Comparator.comparing(Creature::getId));
        List<String> survivors = new ArrayList<>();
        survivors.add("Nr. de turnos terminados:\n");
        survivors.add(String.valueOf(turnos) + "\n\n");
        survivors.add("Ainda pelo bairro:\n\n");
        survivors.add("OS VIVOS:\n");
        for (Creature criatura : criaturas) {
            if (criatura.getEquipa() == 10) {
                survivors.add(criatura.getId() + " " + criatura.getNome() + "\n\n");
            }
        }
        survivors.add("OS OUTROS:\n");
        for (Creature criatura : criaturas) {
            if (criatura.getEquipa() == 20) {
                survivors.add(criatura.getId() + " (antigamente conhecido como <Nome da criatura>)\n\n");
            }
        }
        survivors.add("Num safe haven:\n\n");
        survivors.add("OS VIVOS:\n");
        for (Creature criatura : safeHeaven) {
            if (criatura.getEquipa() == 10) {
                survivors.add(criatura.getId() + " " + criatura.getNome() + "\n\n");
            }
        }
        survivors.add("Envenenados / Destruidos\n\n");
        for (Creature criatura : envenenados) {
            if (criatura.getEquipa() == 10) {
                survivors.add(criatura.getId() + " " + criatura.getNome() + "\n\n");
            }
        }
        survivors.add("OS OUTROS:\n");
        for (Creature criatura : envenenados) {
            if (criatura.getEquipa() == 20) {
                survivors.add(criatura.getId() + " (antigamente conhecido como <Nome da criatura>)\n\n");
            }
        }
        return survivors;
    }

    public boolean isDay() {
        if (turnos == 2 || turnos == 3 || turnos == 6 || turnos == 7 || turnos == 10 || turnos == 11) {
            return false;
        }
        return true;
    }

    public int getEquipmentId(int creatureId) {
        for (Creature criatura : criaturas) {
            for (Equipamento equipamento : equipamentos) {
                if (criatura.getId() == creatureId) {
                    if (criatura.getIdEquipamento() == equipamento.getId()) {
                        return equipamento.getId();
                    }
                }
            }
        }
        return 0;
    }

     public List<Integer> getIdsInSafeHaven() {
        List<Integer> ids = new ArrayList<>();
        for(Creature criatura: safeHeaven) {
            ids.add(criatura.getId());
        }
        return ids;
     }

    public boolean isDoorToSafeHaven(int x, int y) {
        if (tabuleiro[y][x] == 99) {
            return true;
        }
        return false;
    }

    public int getEquipmentTypeId(int equipmentId) {
        for (Equipamento equipamento : equipamentos) {
            if (equipamento.getId() == equipmentId) {
                return equipamento.getIdTipo();
            }
        }
        return 0;
    }

    public String getEquipmentInfo(int equipmentId) {
        String descricao = "";
        for (Equipamento equipamento : equipamentos) {
            if (equipamento.getId() == equipmentId) {
                descricao = equipamento.getDescricao();
            }
        }
        if (equipmentId == 0) {
            descricao = descricao + " | 1";
        } else if (equipmentId == 1 || equipmentId == 2) {
            descricao = descricao + " | 3";
        }
        return descricao;
    }

    public boolean saveGame(File fich) {
        // Por Implementar -> falta escrever para dentro do ficheiro
        try {
            BufferedReader leitorFicheiro = null;
            leitorFicheiro = new BufferedReader(new FileReader(fich.getPath()));
            for(Creature criatura: criaturas) {
                criatura.setEquipmentId(0);
            }
            startGame(fich);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean loadGame(File fich) {
        try {
            BufferedReader leitorFicheiro = null;
            leitorFicheiro = new BufferedReader(new FileReader(fich.getPath()));
            for(Creature criatura: criaturas) {
                criatura.setEquipmentId(0);
            }
            startGame(fich);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String[] popCultureExtravaganza() {
        String[] strings = new String[0];
        return strings;
    }



}
