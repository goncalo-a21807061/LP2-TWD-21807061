package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.util.*;

public class TWDGameManager {

    private static List<Creature> criaturas = new ArrayList<>();
    private static List<Humano> humanos = new ArrayList<>();
    private static List<Zombie> zombies = new ArrayList<>();
    private static List<Equipamento> equipamentos = new ArrayList<>();
    private static List<Equipamento> equipamentosRemove = new ArrayList<>();
    private static List<Porta> portas = new ArrayList<>();
    private static List<Creature> safeHeaven = new ArrayList<>();
    private static List<Creature> envenenados = new ArrayList<>();

    private int[][] tabuleiro;
    private int width;
    private int height;
    private int rows;
    private int columns;

    private int equipaInicial;
    private int currentTeam;
    private int turnos = 0;
    private int turnosGameIsOver = 0;
    private int nrCriaturas;
    private int nrEquipamentos;
    private int nrPortas;
    private int idTipoEquipamento;
    private int bala;
    private int turnosVeneno = 0;
    private int idTipo;
    private boolean moveDiagonal;
    private boolean antidoto = false;
    private boolean venenoUsado = false;


    public TWDGameManager() {
    }


    public boolean startGame(File ficheiroInicial) {
        BufferedReader leitorFicheiro = null;
        String linha;
        turnos = 0;
        turnosGameIsOver = 0;
        turnosVeneno = 0;
        antidoto = false;
        venenoUsado = false;
        int count = 0, count1 = 0, count2 = 0;
        int id, idTipo, x, y;
        criaturas = new ArrayList<>();
        humanos = new ArrayList<>();
        zombies = new ArrayList<>();
        equipamentos = new ArrayList<>();
        equipamentosRemove = new ArrayList<>();
        envenenados = new ArrayList<>();
        portas = new ArrayList<>();
        safeHeaven = new ArrayList<>();
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
                    equipaInicial = Integer.parseInt(linha);
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
                        if(idTipo == 0 || idTipo == 1 || idTipo == 2 || idTipo == 3 || idTipo == 4) {
                            Creature criatura = new Zombie(id, idTipo, nome, x, y);
                            criaturas.add(criatura);
                            zombies.add((Zombie) criatura);
                        } else {
                            Creature criatura = new Humano(id,idTipo,nome,x,y);
                            criaturas.add(criatura);
                            humanos.add((Humano) criatura);
                        }
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
                        equipamentosRemove.add(equipamento);
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
        int id, idEquipamento,idHumano, idZombie = 30;
        if (!gameIsOver()) {
            if(xO < 0 || yO < 0 || xD < 0 || yD < 0) {
                return false;
            }
            if(antidoto == true) {
                turnosVeneno++;
            }
            if(turnosVeneno > 2) {
                antidoto = false;
                for(Creature criatura : criaturas) {
                    if(criatura.getEnvenenado() == true) {
                        tabuleiro[criatura.getY()][criatura.getX()] = 0;
                        criatura.setLocal("morta");
                        humanos.remove(criatura);
                        envenenados.add(criatura);
                    }
                }
            }
            //se for turno dos humanos nao pode deixar mover zombies e vice-versa
            if (currentTeam == 10) {
                for (Creature humano : criaturas) {
                    if (humano.getEquipa() == 10) {
                        for (Equipamento equipamento : equipamentos) {
                            for (Creature zombie : criaturas) {
                                moveDiagonal = humano.getMoverDiagonal();
                                if(zombie.getEquipa() == 20) {
                                    idZombie = zombie.getId();
                                }
                                idEquipamento = equipamento.getId();
                                idTipo = humano.getIdTipo();
                                idTipoEquipamento = equipamento.getIdTipo();
                                id = humano.getId();
                                if (tabuleiro[yO][xO] == id) {
                                    // validar se é Idoso Humano -> Se for só pode jogar nos turnos diurnos e verificar que não se pode mover na diagonal
                                    if(idTipo == 8 && isDay() == true) {
                                        if ((Math.abs(xO - xD) <= humano.getAlcance() && Math.abs(yO - yD) <= humano.getAlcance()) && !(Math.abs(xD - xO) > 0 && Math.abs(yD - yO) > 0)) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                if(humano.getIdEquipamento() != 0) {
                                                    tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                    humano.setEquipmentId(0);
                                                }
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                if(equipamento.getIdTipo() == 8 && venenoUsado == false) {
                                                    antidoto = true;
                                                    venenoUsado = true;
                                                    humano.setEnvenenado(true);
                                                }
                                                if(equipamento.getIdTipo() == 9 && antidoto == false) {
                                                    return false;
                                                }
                                                if(equipamento.getIdTipo() == 9 && antidoto == true) {
                                                    antidoto = false;
                                                }
                                                humano.adicionaEquipamentosEncontrados(1);
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = idEquipamento;
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setEquipmentId(idEquipamento);
                                                humano.setIdTipoEquipamento(idTipoEquipamento);
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == 99) {
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                humano.setEquipmentId(0);
                                                humano.setLocal("safe haven");  // toString
                                                safeHeaven.add(humano);
                                                humanos.remove(humano);
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idZombie) {
                                                if (zombie.getEquipa() == 20) {
                                                    if (humano.getIdEquipamento() == 0) {
                                                        return false;
                                                    }
                                                }
                                                if (humano.getIdTipoEquipamento() == 0) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 1) {
                                                    humano.setX(xD);
                                                    humano.setY(yD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    zombie.setLocal("morta");
                                                    envenenados.add(zombie);
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 2) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (bala == 0) {
                                                                //   equipamentos.remove(equipamento1);
                                                                humano.setEquipmentId(0);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                humano.setX(xD);
                                                                humano.setY(yD);
                                                                tabuleiro[yO][xO] = 0;
                                                                tabuleiro[yD][xD] = id;
                                                                zombie.setLocal("morta");
                                                                envenenados.add(zombie);
                                                                equipamento1.setDuracao(1);
                                                            }
                                                            if(zombie.getIdTipo() == 4) {
                                                                return false;
                                                            }
                                                        }
                                                    }
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 3) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 4) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 5) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 6) {
                                                    humano.setX(xD);
                                                    humano.setY(yD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    zombie.setLocal("morta");
                                                    envenenados.add(zombie);
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 7) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 8) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 9) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 10) {
                                                    humano.setX(xD);
                                                    humano.setY(yD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    zombie.setLocal("morta");
                                                    envenenados.add(zombie);
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                }
                                            }
                                        }
                                    } else if(idTipo == 9) {
                                        if (((Math.abs(xD -xO) > 0 && Math.abs(xD -xO) <= 2) && (Math.abs(yD - yO) > 0 && Math.abs(yD - yO) <= 2))) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                if(equipamento.getIdTipo() == 8 && venenoUsado == false) {
                                                    antidoto = true;
                                                    venenoUsado = true;
                                                    humano.setEnvenenado(true);
                                                }
                                                if(equipamento.getIdTipo() == 9 && antidoto == false) {
                                                    return false;
                                                }
                                                if(equipamento.getIdTipo() == 9 && antidoto == true) {
                                                    antidoto = false;
                                                }
                                                humano.adicionaEquipamentosEncontrados(1);
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = idEquipamento;
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setIdTipoEquipamento(idTipoEquipamento);
                                                humano.setEquipmentId(idEquipamento);
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == 99) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setEquipmentId(0);
                                                humano.setLocal("safe haven");  // toString
                                                safeHeaven.add(humano);
                                                humanos.remove(humano);
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idZombie) {
                                                if (verificarSobrePosicao(xO, xD, yO, yD) == false) {
                                                    return false;
                                                }
                                                if (zombie.getEquipa() == 20) {
                                                    if (humano.getIdEquipamento() == 0) {
                                                        return false;
                                                    }
                                                }
                                                if (humano.getIdTipoEquipamento() == 0) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 1) {
                                                    if (humano.getIdTipo() == 5) {
                                                        idZombie = tabuleiro[yD][xD];
                                                        for (Creature zombie1 : criaturas) {
                                                            if (idZombie == zombie1.getId()) {
                                                                idZombie = zombie1.getIdTipo();
                                                            }
                                                        }
                                                        if (idZombie != 0) {
                                                            return false;
                                                        } else {
                                                            humano.setX(xD);
                                                            humano.setY(yD);
                                                            tabuleiro[yO][xO] = 0;
                                                            tabuleiro[yD][xD] = id;
                                                            zombie.setLocal("morta");
                                                            envenenados.add(zombie);
                                                        }
                                                    } else {
                                                        humano.setX(xD);
                                                        humano.setY(yD);
                                                        tabuleiro[yO][xO] = 0;
                                                        tabuleiro[yD][xD] = id;
                                                        zombie.setLocal("morta");
                                                        envenenados.add(zombie);
                                                    }
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 2) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                humano.setX(xD);
                                                                humano.setY(yD);
                                                                tabuleiro[yO][xO] = 0;
                                                                tabuleiro[yD][xD] = id;
                                                                zombie.setLocal("morta");
                                                                envenenados.add(zombie);
                                                                equipamento1.setDuracao(1);
                                                            } else if (bala == 0) {
                                                                //  equipamentos.remove(equipamento1);
                                                                humano.setEquipmentId(0);
                                                                return false;
                                                            } else if (zombie.getIdTipo() == 4) {
                                                                return false;
                                                            }
                                                        }
                                                    }
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 3) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 4) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 5) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 6) {
                                                    humano.setX(xD);
                                                    humano.setY(yD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    zombie.setLocal("morta");
                                                    envenenados.add(zombie);
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 7) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 8) {
                                                    return false;
                                                   /*
                                                    antidoto = true;
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
                                                    */
                                                } else if (humano.getIdTipoEquipamento() == 9) {
                                                    return false;
                                                    /*
                                                    if (antidoto) {
                                                        tabuleiro[yD][xD] = 0;
                                                        humano.setEquipmentId(0);
                                                        tabuleiro[yD][xD] = id;
                                                        // equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                     */
                                                } else if (humano.getIdTipoEquipamento() == 10) {
                                                    humano.setX(xD);
                                                    humano.setY(yD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                }
                                            }
                                        }

                                    } else if (idTipo != 8 && idTipo != 9){
                                        if(idTipo == 5 && (Math.abs(xO - xD) > 0 && Math.abs(yO-yD) > 0)) {
                                            return false;
                                        }
                                        if ((Math.abs(xO - xD) <= humano.getAlcance() && Math.abs(yO - yD) <= humano.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                //alcance diagonal Adulto
                                                if(humano.getIdTipo() == 6) {
                                                    if (xD - xO > 2 && yO - yD > 2) {
                                                        return false;
                                                    }

                                                    // alcance diagonal Militar
                                                } else if(humano.getIdTipo() == 7) {
                                                    if (xD - xO > 3 && yO - yD > 3) {
                                                        return false;
                                                    }
                                                    if (verificarSobrePosicao(xO, xD, yO, yD) == false) {
                                                        return false;
                                                    }
                                                }
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                if(equipamento.getIdTipo() == 8 && venenoUsado == false) {
                                                    antidoto = true;
                                                    venenoUsado = true;
                                                    humano.setEnvenenado(true);
                                                }
                                                if(equipamento.getIdTipo() == 9 && antidoto == false) {
                                                    return false;
                                                }
                                                if(equipamento.getIdTipo() == 9 && antidoto == true) {
                                                    antidoto = false;
                                                }
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yD][xD] = idEquipamento;
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.adicionaEquipamentosEncontrados(1);
                                                humano.setEquipmentId(idEquipamento);
                                                humano.setIdTipoEquipamento(idTipoEquipamento);
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == 99) {
                                                /*
                                                if(verificarSobrePosicao(xO,xD,yO,yD)) {
                                                    return false;
                                                }
                                                 */
                                                humano.setX(xD);
                                                humano.setY(yD);
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setEquipmentId(0);
                                                humano.setLocal("safe haven");  // toString
                                                safeHeaven.add(humano);
                                                humanos.remove(humano);
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idZombie) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                if (zombie.getEquipa() == 20) {
                                                    if (humano.getIdEquipamento() == 0) {
                                                        return false;
                                                    }
                                                }
                                                if (humano.getIdTipoEquipamento() == 0) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 1) {
                                                    if (humano.getIdTipo() == 5) {
                                                        idZombie = tabuleiro[yD][xD];
                                                        for (Creature zombie1 : criaturas) {
                                                            if (idZombie == zombie1.getId()) {
                                                                idZombie = zombie1.getIdTipo();
                                                            }
                                                        }
                                                        if (idZombie != 0) { //diferente de criança
                                                            return false;
                                                        } else {
                                                            humano.setX(xD);
                                                            humano.setY(yD);
                                                            tabuleiro[yO][xO] = 0;
                                                            tabuleiro[yD][xD] = id;
                                                            zombie.setLocal("morta");
                                                            envenenados.add(zombie);
                                                        }
                                                    } else {
                                                        humano.setX(xD);
                                                        humano.setY(yD);
                                                        tabuleiro[yO][xO] = 0;
                                                        tabuleiro[yD][xD] = id;
                                                        zombie.setLocal("morta");
                                                        envenenados.add(zombie);
                                                    }
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 2) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                humano.setX(xD);
                                                                humano.setY(yD);
                                                                tabuleiro[yO][xO] = 0;
                                                                tabuleiro[yD][xD] = id;
                                                                zombie.setLocal("morta");
                                                                envenenados.add(zombie);
                                                                equipamento1.setDuracao(1);
                                                            } else if (bala == 0) {
                                                                //  equipamentos.remove(equipamento1);
                                                                humano.setEquipmentId(0);
                                                                return false;
                                                            } else if(zombie.getIdTipo() == 4) {
                                                                return false;
                                                            }
                                                        }
                                                    }
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 3) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 4) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 5) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 6) {
                                                    humano.setX(xD);
                                                    humano.setY(yD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    zombie.setLocal("morta");
                                                    envenenados.add(zombie);
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 7) {
                                                    return false;
                                                } else if (humano.getIdTipoEquipamento() == 8) {
                                                    return false;
                                                   /*
                                                    antidoto = true;
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
                                                    */
                                                } else if (humano.getIdTipoEquipamento() == 9) {
                                                    return false;
                                                    /*
                                                    if (antidoto) {
                                                        tabuleiro[yD][xD] = 0;
                                                        humano.setEquipmentId(0);
                                                        tabuleiro[yD][xD] = id;
                                                        // equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                     */
                                                } else if (humano.getIdTipoEquipamento() == 10) {
                                                    humano.setX(xD);
                                                    humano.setY(yD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    zombie.setLocal("morta");
                                                    envenenados.add(zombie);
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
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
                                idEquipamento = equipamento.getId();
                                idTipoEquipamento = equipamento.getIdTipo();
                                id = zombie.getId();
                                if (tabuleiro[yO][xO] == id) {
                                    // validar se é zombie vampiro, se for só pode jogar nos turnos noturnos
                                    if(idTipo == 4 && isDay() == false) {
                                        if ((Math.abs(xO - xD) <= zombie.getAlcance() && Math.abs(yO - yD) <= zombie.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                if (xD - xO > 2 && yO - yD > 2) {
                                                    return false;
                                                }
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                zombie.setX(xD);
                                                zombie.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 20) {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                if (idTipoEquipamento == 5) {
                                                    return false;
                                                } else if(equipamento.getIdTipo() == 8) {
                                                    return false;
                                                } else {
                                                    zombie.adicionaEquipamentosEncontrados(1);
                                                    zombie.setX(xD);
                                                    zombie.setY(yD);
                                                    tabuleiro[yD][xD] = id;
                                                    tabuleiro[yO][xO] = 0;
                                                    equipamentosRemove.remove(equipamento);
                                                    turnos++;
                                                    turnosGameIsOver++;
                                                    if (currentTeam == 20) {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                }
                                            }
                                            if (tabuleiro[yD][xD] == idHumano) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                if (humano.getEquipa() == 10) {
                                                    if(humano.getIdTipo() == 9) {
                                                    }
                                                    if (humano.getIdEquipamento() == 0) {
                                                        turnosGameIsOver = -1;
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.setEquipa(20);
                                                        humanos.remove(humano);
                                                        humano.colocaAZeroEquipamentos();
                                                        humano.humanoParaZombie();
                                                        humano.setImagePNG("zombie.png");
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                    if (humano.getIdTipoEquipamento() == 0) {
                                                        humano.setEquipmentId(0);
                                                        equipamentosRemove.remove(equipamento);
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 1) {
                                                        if (humano.getIdTipo() == 5) {
                                                            idZombie = tabuleiro[yD][xD];
                                                            for (Creature zombie1 : criaturas) {
                                                                if (idZombie == zombie1.getId()) {
                                                                    idZombie = zombie1.getIdTipo();
                                                                }
                                                            }
                                                            if (idZombie == 0) {
                                                            }
                                                        } else {
                                                            tabuleiro[yO][xO] = 0;
                                                            zombie.setLocal("morta");
                                                            envenenados.add(zombie);
                                                            turnos++;
                                                            turnosGameIsOver++;
                                                            if (currentTeam == 20) {
                                                                currentTeam = 10;
                                                            }
                                                            return true;
                                                        }
                                                    } else if (humano.getIdTipoEquipamento() == 2) {
                                                        for (Equipamento equipamento1 : equipamentos) {
                                                            if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    tabuleiro[yD][xD] = idHumano;
                                                                    zombie.setLocal("morta");
                                                                    envenenados.add(zombie);
                                                                    equipamento1.setDuracao(1);
                                                                } else if (zombie.getIdTipo() == 4) {
                                                                    return false;
                                                                } else if (bala == 0) {
                                                                    //  equipamentos.remove(equipamento1);
                                                                    humano.setEquipmentId(0);
                                                                    return false;
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 3) {
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 4) {
                                                        if(zombie.getIdTipo() != 3) {// Idoso zombie
                                                            turnosGameIsOver = -1;
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humanos.remove(humano);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.setEquipmentId(0);
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 5) {
                                                        if(zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                            turnosGameIsOver = -1;
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humanos.remove(humano);
                                                            humano.setEquipmentId(0);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 6) {
                                                        tabuleiro[yO][xO] = idHumano;
                                                        tabuleiro[yD][xD] = 0;
                                                        zombie.setLocal("morta");
                                                        envenenados.add(zombie);
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 7) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    //    equipamentos.remove(equipamento1);
                                                                    humano.setEquipmentId(0);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    /*
                                                                    tabuleiro[yO][xO] = 0;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
                                                                     */
                                                                    equipamento1.setDuracao(1);
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 8) {
                                                        //return false;
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;

                                                    } else if (humano.getIdTipoEquipamento() == 9) {
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 10) {
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    } else if (idTipo != 4) {
                                        if((idTipo == 3 || idTipo == 0) && (Math.abs(xO - xD) > 0 && Math.abs(yO-yD) > 0)) {
                                            return false;
                                        }
                                        if ((Math.abs(xO - xD) <= zombie.getAlcance() && Math.abs(yO - yD) <= zombie.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                zombie.setX(xD);
                                                zombie.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 20) {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                if(equipamento.getIdTipo() == 8) {
                                                    return false;
                                                }
                                                zombie.adicionaEquipamentosEncontrados(1);
                                                zombie.setX(xD);
                                                zombie.setY(yD);
                                                tabuleiro[yD][xD] = id;
                                                tabuleiro[yO][xO] = 0;
                                                equipamentosRemove.remove(equipamento);
                                                turnos++;
                                                turnosGameIsOver++;
                                                if (currentTeam == 20) {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idHumano) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
                                                if (humano.getEquipa() == 10) {
                                                    if(humano.getIdTipo() == 9) {
                                                        return false;
                                                    }
                                                    if (humano.getIdEquipamento() == 0) {
                                                        turnosGameIsOver = -1;
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.setEquipa(20);
                                                        humanos.remove(humano);
                                                        humano.colocaAZeroEquipamentos();
                                                        humano.humanoParaZombie();
                                                        humano.setImagePNG("zombie.png");
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                    if (humano.getIdTipoEquipamento() == 0) {
                                                        humano.setEquipmentId(0);
                                                        equipamentosRemove.remove(equipamento);
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 1) {
                                                        tabuleiro[yO][xO] = 0;
                                                        zombie.setLocal("morta");
                                                        envenenados.add(zombie);
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 2) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    //  equipamentos.remove(equipamento1);
                                                                    humano.setEquipmentId(0);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    tabuleiro[yD][xD] = idHumano;
                                                                    zombie.setLocal("morta");
                                                                    envenenados.add(zombie);
                                                                    equipamento1.setDuracao(1);
                                                                }
                                                                if(zombie.getIdTipo() == 4) {
                                                                    return false;
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 3) {
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 4) {
                                                        if(zombie.getIdTipo() == 3) {
                                                        }
                                                        if(zombie.getIdTipo() != 3) {// Idoso zombie
                                                            turnosGameIsOver = -1;
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humanos.remove(humano);
                                                            humano.setEquipmentId(0);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
                                                            equipamentosRemove.remove(equipamento);
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 5) {
                                                        if(zombie.getIdTipo() == 4) {
                                                            return false;
                                                        }
                                                        if(zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                            turnosGameIsOver = -1;
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humanos.remove(humano);
                                                            humano.setEquipmentId(0);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
                                                            equipamentosRemove.remove(equipamento);
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 6) {
                                                        tabuleiro[yO][xO] = idHumano;
                                                        tabuleiro[yD][xD] = 0;
                                                        zombie.setLocal("morta");
                                                        envenenados.add(zombie);
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 7) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    //  equipamentos.remove(equipamento1);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    /*
                                                                    //tabuleiro[yO][xO] = 0;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
                                                                     */
                                                                    equipamento1.setDuracao(1);
                                                                }
                                                            }
                                                        }
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 8) {
                                                        //return false;
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 9) {
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 10) {
                                                        turnos++;
                                                        turnosGameIsOver++;
                                                        if (currentTeam == 20) {
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
        for (Creature creature: criaturas) {
            if(creature.getEquipa() == 10) {
                if(creature.getLocal() != "safe haven" && creature.getLocal() != "morta") {
                    count++;
                }
            }
        }
        if ((turnosGameIsOver >= 6) || count == 0 ) {
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

    public List<Humano> getHumans() {
        return humanos;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public List<Equipamento> getEquipamentos() { return equipamentos;}

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
        survivors.add("Nr. de turnos terminados:");
        survivors.add(String.valueOf(turnos));
        survivors.add("");
        survivors.add("Ainda pelo bairo:");
        survivors.add("");
        survivors.add("OS VIVOS");
        for (Creature criatura : criaturas) {
            if (criatura.getEquipa() == 10) {
                if(criatura.getLocal() != "safe haven" && criatura.getLocal() != "morta") {
                    survivors.add(criatura.getId() + " " + criatura.getNome());
                }
            }
        }
        survivors.add("");
        survivors.add("OS OUTROS");
        for (Creature criatura : criaturas) {
            if (criatura.getEquipa() == 20) {
                survivors.add(criatura.getId() + " (antigamente conhecido como " + criatura.getNome() + ")");
            }
        }
        survivors.add("");
        survivors.add("Num safe haven:");
        survivors.add("");
        survivors.add("OS VIVOS");
        for (Creature criatura : safeHeaven) {
            if (criatura.getEquipa() == 10) {
                if(criatura.getLocal() == "safe haven") {
                    survivors.add(criatura.getId() + " " + criatura.getNome());
                }
            }
        }
        survivors.add("");
        survivors.add("Envenenados / Destruidos");
        survivors.add("");
        survivors.add("OS VIVOS");
        for (Creature criatura : envenenados) {
            if (criatura.getEquipa() == 10) {
                if(criatura.getLocal() == "morta") {
                    survivors.add(criatura.getId() + " " + criatura.getNome());
                }
            }
        }
        survivors.add("");
        survivors.add("OS OUTROS");
        for (Creature criatura : envenenados) {
            if (criatura.getEquipa() == 20) {
                survivors.add(criatura.getId() + " (antigamente conhecido como " + criatura.getNome() + ")");
            }
        }
        return survivors;
    }


    public boolean isDay() {
        int count = 0;
        boolean isDiurno = true;
        for(int i = 0; i < turnos; i++) {
            count++;
            if (count > 1) {
                count = 0;
                if (isDiurno){
                    isDiurno = false;
                } else {
                    isDiurno = true;
                }
            }
        }
        return isDiurno;
    }

    public int getEquipmentId(int creatureId) {
        for (Creature criatura : criaturas) {
            if (criatura.getId() == creatureId) {
                return criatura.getIdEquipamento();
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
        int idTipo;
        for (Equipamento equipamento : equipamentos) {
            if (equipamento.getId() == equipmentId) {
                descricao = equipamento.getDescricao();
                idTipo = equipamento.getIdTipo();
                if (idTipo == 0 || idTipo == 2 || idTipo == 7) {
                    descricao = descricao + " | " + String.valueOf(equipamento.getDuracao());
                }
            }
        }
        return descricao;
    }


    //erro
    public boolean saveGame(File fich) {
        try {
            int count = 0;
            for (Creature creature: criaturas) {
                if(creature.getLocal() != "safe haven" && creature.getLocal() != "morta") {
                    count++;
                }
            }
            String string = rows + " " + columns + "\n" + getCurrentTeamId() + "\n" + count + "\n";
            for (Creature creature: criaturas) {
                if(creature.getLocal() != "safe haven" && creature.getLocal() != "morta") {
                    string += creature.getId() + " : " + creature.getIdTipo() + " : " + creature.getNome() + " : " + creature.getX() + " : " + creature.getY() + "\n";
                }
            }
            string += equipamentosRemove.size() + "\n";
            for(Equipamento equipamento: equipamentosRemove) {
                string += equipamento.getId() + " : " + equipamento.getIdTipo() + " : " + equipamento.getX() + " : " + equipamento.getY() + "\n";
            }
            string += nrPortas + "\n";
            for(Porta porta: portas) {
                string += porta.getX() + " : " + porta.getY() + "\n";
            }
            FileWriter fileWriter = new FileWriter(fich.getPath());
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(string);
            printWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loadGame(File fich) {
        try {
            BufferedReader leitorFicheiro = null;
            leitorFicheiro = new BufferedReader(new FileReader(fich.getPath()));
            startGame(fich);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String[] popCultureExtravaganza() {
        String[] strings = new String[14];
        strings[0] = "Resident Evil";
        strings[1] = "Evil Dead";
        strings[2] = "I Am Legend";
        strings[3] = "I Am Legend";
        strings[4] = "Dragon Ball";
        strings[5] = "World War Z";
        strings[6] = "Mandalorians";
        strings[7] = "1972";
        strings[8] = "Kill Bill";
        strings[9] = "1978";
        strings[10] = "James Bond";
        strings[11] = "The Walking Dead";
        strings[12] = "Cabeça de alho xoxo";
        strings[13] = "Farrokh Bulsara";
        return strings;
    }

    public boolean verificarSobrePosicao(int xO, int xD, int yO, int yD) {
        if((Math.abs(xO-xD) >= 2 && Math.abs(yO-yD) == 1) || (Math.abs(xD-xO) == 1 && Math.abs(yD-yO) >= 2)) {
            return false;
        }
        if(xD-xO > 1 && yO - yD > 1) {
            for (Humano humano1 : humanos) {
                if (tabuleiro[yO - 1][xO + 1] == humano1.getId()) {     // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for (Equipamento equipamento1 : equipamentos) {       // Garantir que nao passa por cima de um equipamento
                if (tabuleiro[yO - 1][xO + 1] == equipamento1.getId()) {
                    return false;
                }
            }
            for (Zombie zombie1 : zombies) {                    // Garantir que nao passa por cima de um zombie
                if (tabuleiro[yO - 1][xO + 1] == zombie1.getId()) {
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO - 1][xO + 1] == 99) {
                    return false;
                }
            }
        }
        if(xO - xD > 1 && yD - yO > 1) {
            for (Humano humano1 : humanos) {
                if (tabuleiro[yO + 1][xO - 1] == humano1.getId()) {     // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for (Equipamento equipamento1 : equipamentos) {       // Garantir que nao passa por cima de um equipamento
                if (tabuleiro[yO + 1][xO - 1] == equipamento1.getId()) {
                    return false;
                }
            }
            for (Zombie zombie1 : zombies) {                    // Garantir que nao passa por cima de um zombie
                if (tabuleiro[yO + 1][xO - 1] == zombie1.getId()) {
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO + 1][xO - 1] == 99) {
                    return false;
                }
            }
        }
        if(xO - xD > 1 && yO - yD > 1) {
            for (Humano humano1 : humanos) {
                if (tabuleiro[yO - 1][xO - 1] == humano1.getId()) {     // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for (Equipamento equipamento1 : equipamentos) {            // Garantir que nao passa por cima de um equipamento
                if (tabuleiro[yO - 1][xO - 1] == equipamento1.getId()) {
                    return false;
                }
            }
            for (Zombie zombie1 : zombies) {                            // Garantir que nao passa por cima de um zombie
                if (tabuleiro[yO - 1][xO - 1] == zombie1.getId()) {
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO - 1][xO - 1] == 99) {
                    return false;
                }
            }
        }
        if(xD - xO > 1 && yD - yO > 1) {
            for (Humano humano1 : humanos) {
                if (tabuleiro[yO + 1][xO + 1] == humano1.getId()) {     // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for (Equipamento equipamento1 : equipamentos) {            // Garantir que nao passa por cima de um equipamento
                if (tabuleiro[yO + 1][xO + 1] == equipamento1.getId()) {
                    return false;
                }
            }
            for (Zombie zombie1 : zombies) {                            // Garantir que nao passa por cima de um zombie
                if (tabuleiro[yO + 1][xO + 1] == zombie1.getId()) {
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO + 1][xO + 1] == 99) {
                    return false;
                }
            }
        }
        if (xD - xO > 1 && Math.abs(yD-yO) == 0 ) {
            for(Humano humano1: humanos) {
                if(tabuleiro[yO][xO+1] == humano1.getId()) {            // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for(Equipamento equipamento1: equipamentos) {              // Garantir que nao passa por cima de um equipamento
                if(tabuleiro[yO][xO+1] == equipamento1.getId()) {
                    return false;
                }
            }
            for(Zombie zombie1 : zombies) {                            // Garantir que não passa por cima de um equipamento
                if(tabuleiro[yO][xO+1] == zombie1.getId()) {
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO][xO+1] == 99) {
                    return false;
                }
            }
        }
        if(xO - xD > 1 && Math.abs(yD-yO) == 0) {
            for(Humano humano1: humanos) {
                if(tabuleiro[yO][xO-1] == humano1.getId()) {          // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for(Equipamento equipamento1: equipamentos) {             // Garantir que nao passa por cima de um equipamento
                if(tabuleiro[yO][xO-1] == equipamento1.getId()) {
                    return false;
                }
            }
            for(Zombie zombie1 : zombies) {
                if(tabuleiro[yO][xO-1] == zombie1.getId()) {         // Garantir que nao passa por cima de um zombie
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO][xO-1] == 99) {
                    return false;
                }
            }
        }
        if(yD - yO > 1 && Math.abs(xD-xO) == 0) {
            for(Humano humano1: humanos) {
                if(tabuleiro[yO+1][xO] == humano1.getId()) {        // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for(Equipamento equipamento1: equipamentos) {          // Garantir que nao passa por cima de um equipamento
                if(tabuleiro[yO+1][xO] == equipamento1.getId()) {
                    return false;
                }
            }
            for(Zombie zombie1 : zombies) {
                if(tabuleiro[yO+1][xO] == zombie1.getId()) {      // Garantir que nao passa por cima de um zombie
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO+1][xO] == 99) {
                    return false;
                }
            }
        }
        if(yO - yD > 1 && Math.abs(xD-xO) == 0) {
            for(Humano humano1: humanos) {
                if(tabuleiro[yO-1][xO] == humano1.getId()) {     // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for(Equipamento equipamento1: equipamentos) {       // Garantir que nao passa por cima de um equipamento
                if(tabuleiro[yO-1][xO] == equipamento1.getId()) {
                    return false;
                }
            }
            for(Zombie zombie1 : zombies) {                    // Garantir que nao passa por cima de um zombie
                if(tabuleiro[yO-1][xO] == zombie1.getId()) {
                    return false;
                }
            }
            for (Porta porta: portas) {                         // Garantir que não passa por cima de uma porta
                if(tabuleiro[yO-1][xO] == 99) {
                    return false;
                }
            }
        }
        return true;
    }
}

