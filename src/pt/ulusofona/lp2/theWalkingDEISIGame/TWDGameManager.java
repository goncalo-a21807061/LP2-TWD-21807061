package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.util.*;

public class TWDGameManager {

    private static List<Creature> criaturas = new ArrayList<>();
    private static List<Humano> humanos = new ArrayList<>();
    private static List<Zombie> zombies = new ArrayList<>();
    private static List<Equipamento> equipamentos = new ArrayList<>();
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
    private int nrCriaturas;
    private int nrEquipamentos;
    private int nrPortas;
    private int idTipoEquipamento;
    private int bala;
    private int turnosVeneno = 0;
    private int idTipo;
    private boolean moveDiagonal;
    private boolean antidoto = false;


    public TWDGameManager() {
    }

    public boolean startGame(File ficheiroInicial) {
        BufferedReader leitorFicheiro = null;
        String linha;
        int count = 0, count1 = 0, count2 = 0;
        int id, idTipo, x, y;
        criaturas = new ArrayList<>();
        humanos = new ArrayList<>();
        zombies = new ArrayList<>();
        equipamentos = new ArrayList<>();
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
                                    if(idTipo == 8 && (turnos == 0 || turnos == 4 || turnos == 8)) {
                                        if ((Math.abs(xO - xD) <= humano.getAlcance() && Math.abs(yO - yD) <= humano.getAlcance()) && !(Math.abs(xD - xO) > 0 && Math.abs(yD - yO) > 0)) {
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
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setEquipmentId(0);
                                                safeHeaven.add(humano);
                                                humano.setLocal("safe haven");  // toString
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
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.colocaAZeroEquipamentos();
                                                        humano.humanoParaZombie();
                                                        humano.setEquipa(20);
                                                        humano.setImagePNG("zombie.png");
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
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
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
                                                                humano.setEquipmentId(0);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                tabuleiro[yO][xO] = 0;
                                                                tabuleiro[yD][xD] = id;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
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
                                                    /*
                                                    if (zombie.getIdTipo() != 3) {// Idoso zombie
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.setEquipa(20);
                                                        humano.setImagePNG("zombie.png");
                                                        //equipamentos.remove(equipamento);
                                                    }

                                                     */
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 5) {
                                                    /*
                                                    if (zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                       // equipamentos.remove(equipamento);
                                                    }
                                                    turnos++;

                                                     */
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 6) {
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
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
                                                                humano.setEquipmentId(0);
                                                                equipamentos.remove(equipamento1);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                tabuleiro[yO][xO] = 0;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
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
                                    } else if(idTipo == 9) {
                                        if (((Math.abs(xD -xO) > 0 && Math.abs(xD -xO) <= 2) && (Math.abs(yD - yO) > 0 && Math.abs(yD - yO) <= 2))) {
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
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setEquipmentId(0);
                                                safeHeaven.add(humano);
                                                humano.setLocal("safe haven");  // toString
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
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.setEquipa(20);
                                                        humano.colocaAZeroEquipamentos();
                                                        humano.humanoParaZombie();
                                                        humano.setImagePNG("zombie.png");
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
                                    } else if (idTipo != 8 && idTipo != 9){
                                        if(idTipo == 0 && (Math.abs(xO - xD) > 0 && Math.abs(yO-yD) > 0)) {
                                            return false;
                                        }
                                        if ((Math.abs(xO - xD) <= humano.getAlcance() && Math.abs(yO - yD) <= humano.getAlcance())) {
                                            if (tabuleiro[yD][xD] == 0) {
                                                if(verificarSobrePosicao(xO,xD,yO,yD) == false) {
                                                    return false;
                                                }
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
                                                if (idTipoEquipamento == 8) {
                                                    antidoto = true;
                                                    turnosVeneno++;
                                                }
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
                                                tabuleiro[yO][xO] = humano.getIdEquipamento();
                                                humano.setEquipmentId(0);
                                                safeHeaven.add(humano);
                                                humano.setLocal("safe haven");  // toString
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
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.setEquipa(20);
                                                        humano.colocaAZeroEquipamentos();
                                                        humano.humanoParaZombie();
                                                        humano.setImagePNG("zombie.png");
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
                                                    return false;
                                                   /*
                                                    humano.setEquipmentId(0);
                                                    equipamentos.remove(equipamento);
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;

                                                    */
                                                } else if (humano.getIdTipoEquipamento() == 1) {
                                                    if(humano.getIdTipo() == 5) {
                                                        idZombie = tabuleiro[yD][xD];
                                                        for(Creature zombie1: criaturas) {
                                                            if(idZombie == zombie1.getId()) {
                                                                idZombie = zombie1.getIdTipo();
                                                            }
                                                        }
                                                        if(idZombie == 0) {
                                                            tabuleiro[yD][xD] = 0;
                                                            envenenados.add(zombie);
                                                            zombie.setLocal("morta");
                                                            turnos++;
                                                            if (currentTeam == 10) {
                                                                currentTeam = 20;
                                                            } else {
                                                                currentTeam = 10;
                                                            }
                                                            return true;
                                                        }
                                                    } else {
                                                        humano.setY(yD);
                                                        humano.setX(xD);
                                                        tabuleiro[yO][xO] = 0;
                                                        tabuleiro[yD][xD] = id;
                                                        envenenados.add(zombie);
                                                        zombie.setLocal("morta");
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    }
                                                } else if (humano.getIdTipoEquipamento() == 2) {
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (bala == 0) {
                                                                equipamentos.remove(equipamento1);
                                                                humano.setEquipmentId(0);
                                                            }
                                                            if(zombie.getIdTipo() == 4) {
                                                                return false;
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                tabuleiro[yO][xO] = 0;
                                                                tabuleiro[yD][xD] = id;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
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
                                                    return false;
                                                    /*
                                                    humano.setY(yD);
                                                    humano.setX(xD);
                                                    tabuleiro[yO][xO] = 0;
                                                    tabuleiro[yD][xD] = id;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                     */
                                                } else if (humano.getIdTipoEquipamento() == 4) {
                                                    if(zombie.getIdTipo() == 3) {
                                                        return false;
                                                    }
                                                    if (zombie.getIdTipo() != 3) {// Idoso zombie
                                                        return false;
                                                        /*
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                        equipamentos.remove(equipamento);
                                                        */
                                                    }
                                                    /*
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                         */
                                                } else if (humano.getIdTipoEquipamento() == 5) {
                                                    if(zombie.getIdTipo() == 4) { // Zombie Vampiro
                                                        return false;
                                                    }
                                                    if (zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                        return false;
                                                        /*
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(humano);
                                                        humano.setLocal("morta");
                                                        equipamentos.remove(equipamento);

                                                         */
                                                    }
                                                    /*
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;

                                                     */
                                                } else if (humano.getIdTipoEquipamento() == 6) {
                                                    tabuleiro[yD][xD] = 0;
                                                    envenenados.add(zombie);
                                                    zombie.setLocal("morta");
                                                    turnos++;
                                                    if (currentTeam == 10) {
                                                        currentTeam = 20;
                                                    } else {
                                                        currentTeam = 10;
                                                    }
                                                    return true;
                                                } else if (humano.getIdTipoEquipamento() == 7) {
                                                    return false;
                                                    /*
                                                    for (Equipamento equipamento1 : equipamentos) {
                                                        if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                            bala = equipamento1.getDuracao();
                                                            if (bala == 0) {
                                                                equipamentos.remove(equipamento1);
                                                            }
                                                            if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                humano.setX(xD);
                                                                humano.setY(yD);
                                                                tabuleiro[yO][xO] = 0;
                                                                tabuleiro[yD][xD] = id;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
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

                                                     */
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
                                                        equipamentos.remove(equipamento);
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
                                idEquipamento = equipamento.getId();
                                id = zombie.getId();
                                if (tabuleiro[yO][xO] == id) {
                                    // validar se é zombie vampiro, se for só pode jogar nos turnos noturnos
                                    if(idTipo == 4 && (turnos == 3 || turnos == 7 || turnos == 11)) {
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
                                                if (currentTeam == 10) {
                                                    currentTeam = 20;
                                                } else {
                                                    currentTeam = 10;
                                                }
                                                return true;
                                            }
                                            if (tabuleiro[yD][xD] == idEquipamento) {
                                                if (idTipoEquipamento == 5 && zombie.getIdTipo() == 4) {
                                                    return false;
                                                }
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
                                                    if(humano.getIdTipo() == 9) {
                                                        return false;
                                                    }
                                                    if (humano.getIdEquipamento() == 0) {
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.setEquipa(20);
                                                        humano.colocaAZeroEquipamentos();
                                                        humano.humanoParaZombie();
                                                        humano.setImagePNG("zombie.png");
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
                                                        if (humano.getIdTipo() == 5) {
                                                            idZombie = tabuleiro[yD][xD];
                                                            for (Creature zombie1 : criaturas) {
                                                                if (idZombie == zombie1.getId()) {
                                                                    idZombie = zombie1.getIdTipo();
                                                                }
                                                            }
                                                            if (idZombie == 0) {
                                                                tabuleiro[yD][xD] = 0;
                                                                tabuleiro[yO][xO] = idHumano;
                                                                envenenados.add(zombie);
                                                                zombie.setLocal("morta");
                                                                turnos++;
                                                                if (currentTeam == 10) {
                                                                    currentTeam = 20;
                                                                } else {
                                                                    currentTeam = 10;
                                                                }
                                                                return true;
                                                            }
                                                        } else {
                                                            tabuleiro[yD][xD] = 0;
                                                            tabuleiro[yO][xO] = idHumano;
                                                            envenenados.add(zombie);
                                                            zombie.setLocal("morta");
                                                            turnos++;
                                                            if (currentTeam == 10) {
                                                                currentTeam = 20;
                                                            } else {
                                                                currentTeam = 10;
                                                            }
                                                            return true;
                                                        }
                                                    } else if (humano.getIdTipoEquipamento() == 2) {
                                                        for (Equipamento equipamento1 : equipamentos) {
                                                            if (humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if (bala == 0) {
                                                                    equipamentos.remove(equipamento1);
                                                                    humano.setEquipmentId(0);
                                                                }
                                                                if (zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    tabuleiro[yD][xD] = idHumano;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
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
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
                                                        }
                                                        if(zombie.getIdTipo() == 3) {
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 5) {
                                                        if (zombie.getIdTipo() == 4) {
                                                            return false;
                                                        }
                                                        if(zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
                                                        }
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 6) {
                                                        tabuleiro[yO][xO] = 0;
                                                        envenenados.add(zombie);
                                                        zombie.setLocal("morta");
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 7) {
                                                        for(Equipamento equipamento1: equipamentos) {
                                                            if(humano.getIdTipoEquipamento() == equipamento1.getIdTipo()) {
                                                                bala = equipamento1.getDuracao();
                                                                if(bala == 0) {
                                                                    equipamentos.remove(equipamento1);
                                                                    humano.setEquipmentId(0);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
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
                                                    if(humano.getIdTipo() == 9) {
                                                        return false;
                                                    }
                                                    if (humano.getIdEquipamento() == 0) {
                                                        humano.setNomeEquipa("Os Outros");
                                                        humano.setEquipa(20);
                                                        humano.colocaAZeroEquipamentos();
                                                        humano.humanoParaZombie();
                                                        humano.setImagePNG("zombie.png");
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
                                                        tabuleiro[yO][xO] = idHumano;
                                                        tabuleiro[yD][xD] = 0;
                                                        envenenados.add(zombie);
                                                        zombie.setLocal("morta");
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
                                                                    humano.setEquipmentId(0);
                                                                }
                                                                if(zombie.getIdTipo() != 4 && bala > 0) {
                                                                    tabuleiro[yO][xO] = 0;
                                                                    tabuleiro[yD][xD] = idHumano;
                                                                    envenenados.add(zombie);
                                                                    zombie.setLocal("morta");
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
                                                        if(zombie.getIdTipo() == 3) {
                                                        }
                                                        if(zombie.getIdTipo() != 3) {// Idoso zombie
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
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
                                                        if(zombie.getIdTipo() == 4) {
                                                            return false;
                                                        }
                                                        if(zombie.getIdTipo() != 4) { // Zombie Vampiro
                                                            humano.setNomeEquipa("Os Outros");
                                                            humano.setEquipa(20);
                                                            humano.colocaAZeroEquipamentos();
                                                            humano.humanoParaZombie();
                                                            humano.setImagePNG("zombie.png");
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
                                                        tabuleiro[yO][xO] = idHumano;
                                                        tabuleiro[yD][xD] = 0;
                                                        envenenados.add(zombie);
                                                        zombie.setLocal("morta");
                                                        turnos++;
                                                        if (currentTeam == 10) {
                                                            currentTeam = 20;
                                                        } else {
                                                            currentTeam = 10;
                                                        }
                                                        return true;
                                                    } else if (humano.getIdTipoEquipamento() == 7) {
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

    public List<Humano> getHumans() {
        return humanos;
    }

    public List<Zombie> getZombies() {
        return zombies;
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
                BufferedWriter writer = new BufferedWriter(new FileWriter(fich.getPath(), true));
                writer.write(rows);
                writer.write(columns);
                writer.write(currentTeam + "\n");
                writer.write(criaturas.size());
                for(Creature criatura : criaturas) {
                    writer.write(criatura.getId() + " : " + criatura.getIdTipo() + " : " + criatura.getNome() + " : " + criatura.getX() + " : " + criatura.getY());
                }
                writer.write(equipamentos.size());
                for(Equipamento equipamento: equipamentos) {
                    writer.write(equipamento.getId() + " : " + equipamento.getIdTipo() + " : " + equipamento.getX() + " : " + equipamento.getY());
                }
                writer.write(portas.size());
                for(Porta porta : portas) {
                    writer.write(porta.getX() + " : " + porta.getY());
                }
                writer.close();
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
        String[] strings = new String[14];
        strings[0] = "Resident Evil";
        strings[1] = "Evil Dead";
        strings[2] = "Ash vs Evil Dead";
        strings[3] = "King Of The Zombies";
        strings[4] = "Astérix e Obélix";
        strings[5] = "Invasão Zumbi";
        strings[6] = "Mandalorians";
        strings[7] = "Game of Death";
        strings[8] = "Kill Bill";
        strings[9] = "1978";
        strings[10] = "Sean Connery";
        strings[11] = "The Walking Dead";
        strings[12] = "Cabeça de alho xoxo";
        strings[13] = "Farrokh Bulsara";
        return strings;
    }

    public boolean verificarSobrePosicao(int xO, int xD, int yO, int yD) {
        if (xD - xO > 1) {
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
        }
        if(xO - xD > 1) {
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
        }
        if(yD - yO > 1) {
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
        }
        if(yO - yD > 1) {
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
        }
        if(xD-xO > 1 && yO - yD > 1) {
            for (Humano humano1 : humanos) {
                if (tabuleiro[yO + 1][xO + 1] == humano1.getId()) {     // Garantir que não passa por cima de um humano
                    return false;
                }
            }
            for (Equipamento equipamento1 : equipamentos) {       // Garantir que nao passa por cima de um equipamento
                if (tabuleiro[yO + 1][xO + 1] == equipamento1.getId()) {
                    return false;
                }
            }
            for (Zombie zombie1 : zombies) {                    // Garantir que nao passa por cima de um zombie
                if (tabuleiro[yO + 1][xO + 1] == zombie1.getId()) {
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
        }
        return true;
    }
}
